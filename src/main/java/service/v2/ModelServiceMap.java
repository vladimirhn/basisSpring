package service.v2;

import kpersistence.v2.tables.StringIdTable;

import java.util.HashMap;
import java.util.Map;

public interface ModelServiceMap {
    Map<Class<? extends StringIdTable>, AbstractStringIdTableService<? extends StringIdTable>> data = new HashMap<>();
}
