package com.example.ChangeDataCapture.infrastructure.outputport;

import java.util.List;
import java.util.Map;

public interface QueryRepository {
    public void save(Map<String,Object> reg, Class<?> c);
    public void delete(String id, Class<?> c);
    public Map<String,Object> getById(String id, Class<?> c);
    public List<Map<String,Object>> getAll(Class<?> c);
}
