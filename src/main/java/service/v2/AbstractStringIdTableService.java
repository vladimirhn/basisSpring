package service.v2;

import kcollections.KList;
import repository.v2.AbstractStringIdTableRepository;
import repository.v2.ModelRepositoryMap;
import kpersistence.v2.tables.StringIdTable;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractStringIdTableService <T extends StringIdTable> {

    Class<T> model;
    AbstractStringIdTableRepository<T> repository;

    public AbstractStringIdTableService() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelServiceMap.data.put(model, this);
    }

    private AbstractStringIdTableRepository<T> repository() {
        if (repository == null) {
            repository = (AbstractStringIdTableRepository<T>) ModelRepositoryMap.data.get(model);
        }
        return repository;
    }

    public KList<T> selectAll() {
        return repository().selectAll();
    }

    public KList<T> selectFiltered(T data) {
        return repository().selectFiltered(data);
    }

    public String insert(T data) {
        return repository().insert(data);
    }

    public void update(T data) {
        repository().update(data);
    }

    public String delete(String id) {
        return repository().delete(id);
    }
}
