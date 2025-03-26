package com.finance.repository;
import com.finance.domain.BaseEntity;
import java.io.*;
import java.util.*;

public class FileBasedRepository<T extends BaseEntity<?>> implements Repository<T> {
    private final File file;
    private final Map<Object, T> cache = new HashMap<>();

    public FileBasedRepository(String filePath) throws IOException {
        this.file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("Could not create directory: " + parent);
            }
        }
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Could not create file: " + filePath);
        }
        load();
    }

    private void load() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) cache.putAll((Map<Object, T>) obj);
        } catch (EOFException ignored) {}
        catch (ClassNotFoundException e) { throw new IOException(e); }
    }

    private void persist() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(cache);
        }
    }

    @Override public T save(T entity) { cache.put(entity.getId(), entity); persistUnchecked(); return entity; }
    @Override public T findById(Object id) { return Optional.ofNullable(cache.get(id)).orElseThrow(() -> new NoSuchElementException("Entity not found: " + id)); }
    @Override public List<T> findAll() { return new ArrayList<>(cache.values()); }
    @Override public T update(T entity) { if (!cache.containsKey(entity.getId())) throw new NoSuchElementException("Entity not found"); cache.put(entity.getId(), entity); persistUnchecked(); return entity; }
    @Override public void delete(Object id) { if (cache.remove(id) == null) throw new NoSuchElementException("Entity not found"); persistUnchecked(); }

    private void persistUnchecked() {
        try { persist(); } catch (IOException e) { throw new RuntimeException("Failed to persist data", e); }
    }
}