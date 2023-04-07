package com.example.ChangeDataCapture.infrastructure.outputport;

import java.util.List;

public interface CommandRepository {
    public<T> T save(T reg);
    public<T> T getById(String id, Class<T> c);
    public<T> List<T> getAll(Class<T> c);
}
