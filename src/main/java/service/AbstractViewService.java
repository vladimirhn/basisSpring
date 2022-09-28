package service;

import kcollections.KList;
import kpersistence.tables.UserIdView;
import repository.AbstractViewRepository;
import repository.ModelRepositoryMap;

import java.lang.reflect.ParameterizedType;

public class AbstractViewService<T extends UserIdView> extends AbstractService<T> {

    protected Class<T> model;
    private AbstractViewRepository<T> repository;

    public AbstractViewService() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelServiceMap.data.put(model, this);
    }

    public AbstractViewService(Class<T> model) {
        this.model = model;
    }

    protected AbstractViewRepository<T> repository() {
        if (repository == null) {
            repository = (AbstractViewRepository<T>) ModelRepositoryMap.data.get(model);

            if (repository == null) {
                repository = new AbstractViewRepository<>(model);
            }
        }
        return repository;
    }

    public KList<T> selectAll() {
        return selectAll(null, null);
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

    public KList<T> selectFilteredLabels(T data) {
        return repository().selectFilteredLabels(data);
    }

}
