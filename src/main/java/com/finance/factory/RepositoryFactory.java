package com.finance.factory;
import com.finance.domain.BaseEntity;
import com.finance.repository.*;

import java.io.IOException;

public class RepositoryFactory {
    public static <T extends BaseEntity<?>> Repository<T> createInMemory() { return new InMemoryRepository<>(); }
    public static <T extends BaseEntity<?>> Repository<T> createFileBased(String path) throws IOException { return new FileBasedRepository<>(path); }
}