package service;

import kpersistence.tables.Table;

import java.util.HashMap;
import java.util.Map;

public interface ModelServiceMap {
    Map<Class<? extends Table>, AbstractService<? extends Table>> data = new HashMap<>();
}
