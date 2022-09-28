package service.v2;

import kcollections.KList;
import kpersistence.v2.tables.StringIdTable;
import repository.v2.AbstractStringIdTableRepository;
import repository.v2.ModelRepositoryMap;

import java.lang.reflect.ParameterizedType;
import java.util.function.BiConsumer;

public class AbstractStringIdTableService <T extends StringIdTable> extends AbstractService<T> {

    protected Class<T> model;
    private AbstractStringIdTableRepository<T> repository;

    public AbstractStringIdTableService() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelServiceMap.data.put(model, this);
    }

    public AbstractStringIdTableService(Class<T> model) {
        this.model = model;
    }

    protected AbstractStringIdTableRepository<T> repository() {
        if (repository == null) {
            repository = (AbstractStringIdTableRepository<T>) ModelRepositoryMap.data.get(model);

            if (repository == null) {
                repository = new AbstractStringIdTableRepository<>(model);
            }
        }
        return repository;
    }

    public KList<T> selectAll(String orderByFieldName, String direction) {
        return repository().selectAll(orderByFieldName, direction);
    }

    public KList<T> selectAllLabels(String orderByFieldName, String direction) {
        return repository().selectAllLabels(orderByFieldName, direction);
    }

    public KList<T> selectFiltered(T data) {
        return repository().selectFiltered(data);
    }

    public T selectOne(String id) throws Exception {
        return repository().selectOne(id);
    }

    public KList<T> selectFilteredLabels(T data) {
        return repository().selectFilteredLabels(data);
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

    public <V> String deleteByField(BiConsumer<T, V> fieldSetter, V fieldValue) {
        return repository().deleteByField(fieldSetter, fieldValue);
    }
}
