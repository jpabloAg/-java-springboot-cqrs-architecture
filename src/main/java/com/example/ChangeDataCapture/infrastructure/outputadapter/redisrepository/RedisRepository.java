package com.example.ChangeDataCapture.infrastructure.outputadapter.redisrepository;

import com.example.ChangeDataCapture.domain.Customer;
import com.example.ChangeDataCapture.domain.Order;
import com.example.ChangeDataCapture.infrastructure.outputport.QueryRepository;
import com.example.ChangeDataCapture.infrastructure.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RedisRepository implements QueryRepository {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public void save(Map<String, Object> reg, Class<?> c) {
        redisTemplate
                .opsForHash()
                .put(
                        getHashFromClass(c),
                        reg.get("id"),
                        ConversionUtils.map2Jsonstring(reg));
        if(c.equals(Order.class))
            addOrderToCustomer(reg,true);
    }

    @Override
    public Map<String, Object> getById(String id, Class<?> c) {
        return ConversionUtils.jsonstring2Map(
                (String) redisTemplate.opsForHash().get(
                        getHashFromClass(c),
                        id
                )
        );
    }

    @Override
    public List<Map<String, Object>> getAll(Class<?> c) {
        return redisTemplate.opsForHash()
                .values(getHashFromClass(c))
                .stream()
                .map(l -> ConversionUtils.jsonstring2Map((String) l))
                .collect( Collectors.toList() );
    }

    @Override
    public void delete(String id, Class<?> c) {
        if (c.equals(Order.class)) addOrderToCustomer(id, false);
        redisTemplate.opsForHash().delete(
                getHashFromClass(c),
                id
        );
    }

    private String getHashFromClass(Class<?> c) {
        return c.getName().replace('.', '_');
    }

    private void addOrderToCustomer(String orderId, boolean appendOrder) {
        addOrderToCustomer(
                getById(orderId, Order.class),
                appendOrder
        );
    }

    private void addOrderToCustomer(Map<String, Object> order, boolean appendOrder) {
        String customerId = (String) order.get("customerid");
        if (customerId == null) return;

        Map<String, Object> customer = ConversionUtils.jsonstring2Map(
                (String) redisTemplate.opsForHash().get(
                        getHashFromClass( Customer.class ),
                        customerId
                )
        );

        List<Map<String, Object>> orders = (List<Map<String, Object>>) customer.get("orders");
        if ( orders == null ) orders = new ArrayList<>();

        orders = orders.stream()
                .filter(o -> !((Map<String, Object>)o).get("id").equals( order.get("id")))
                .collect(Collectors.toList());

        if (appendOrder) orders.add(order);

        customer.put("orders", orders);

        redisTemplate.opsForHash().put(
                getHashFromClass(Customer.class),
                customerId,
                ConversionUtils.map2Jsonstring(customer)
        );
    }
}
