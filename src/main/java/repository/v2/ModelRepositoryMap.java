package repository.v2;

import kpersistence.v2.tables.StringIdTable;

import java.util.HashMap;
import java.util.Map;

public interface ModelRepositoryMap {
    Map<Class<? extends StringIdTable>, AbstractStringIdTableRepository<? extends StringIdTable>> data = new HashMap<>();
}
