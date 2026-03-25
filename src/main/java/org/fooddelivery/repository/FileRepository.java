package org.fooddelivery.repository;

import com.google.gson.reflect.TypeToken;
import org.fooddelivery.util.SerializationUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class FileRepository<T> implements IRepository<T> {

    private final String filePath;
    private final Type listType;

    public FileRepository(String filePath, Type listType) {
        this.filePath = filePath;
        this.listType = listType;
    }

    protected List<T> loadAll() {
        List<T> data = SerializationUtils.loadFromFile(filePath, listType);
        return data != null ? data : new ArrayList<>();
    }

    protected void saveAll(List<T> data) {
        SerializationUtils.saveToFile(data, filePath);
    }

    @Override
    public void save(T entity) {
        List<T> all = loadAll();
        all.add(entity);
        saveAll(all);
    }

    @Override
    public void update(T entity) {
        List<T> all = loadAll();
        String id = getId(entity);
        for (int i = 0; i < all.size(); i++) {
            if (getId(all.get(i)).equals(id)) {
                all.set(i, entity);
                break;
            }
        }
        saveAll(all);
    }

    @Override
    public void delete(String id) {
        List<T> all = loadAll();
        all.removeIf(entity -> getId(entity).equals(id));
        saveAll(all);
    }

    @Override
    public Optional<T> findById(String id) {
        return loadAll().stream()
                .filter(entity -> getId(entity).equals(id))
                .findFirst();
    }

    @Override
    public List<T> findAll() {
        return loadAll();
    }

    protected abstract String getId(T entity);
}