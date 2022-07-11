package repository.v2;

import kpersistence.v2.tables.Table;

import java.util.HashMap;
import java.util.Map;

public interface ModelRepositoryMap {
    Map<Class<? extends Table>, AbstractRepository<? extends Table>> data = new HashMap<>();
}
