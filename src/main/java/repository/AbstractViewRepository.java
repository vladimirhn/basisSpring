package repository;

import kpersistence.mapping.MapperAllDataByModel;
import kpersistence.mapping.MapperLabelsByModel;
import kpersistence.tables.UserIdView;

import java.lang.reflect.ParameterizedType;

public class AbstractViewRepository<T extends UserIdView> extends AbstractRepository<T> {

    public AbstractViewRepository() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelRepositoryMap.data.put(model, this);
        allDataRowMapper = new MapperAllDataByModel<>(model)::mapRow;
        labelsRowMapper = new MapperLabelsByModel<>(model)::mapRow;
    }
    public AbstractViewRepository(Class<T> model) {
        super(model);
    }
}
