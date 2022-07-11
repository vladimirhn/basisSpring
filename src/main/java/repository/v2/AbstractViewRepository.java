package repository.v2;

import application.ContextProvider;
import kcollections.CollectionFactory;
import kcollections.KList;
import kpersistence.v2.CurrentUserIdProvider;
import kpersistence.v2.RandomId;
import kpersistence.v2.UnnamedParametersQuery;
import kpersistence.v2.mapping.MapperAllDataByModel;
import kpersistence.v2.mapping.MapperLabelsByModel;
import kpersistence.v2.modelsMaster.ModelsMaster;
import kpersistence.v2.queryGeneration.DeleteQueryGenerator;
import kpersistence.v2.queryGeneration.InsertQueryGenerator;
import kpersistence.v2.queryGeneration.UpdateQueryGenerator;
import kpersistence.v2.queryGeneration.select.SelectAllQueryGenerator;
import kpersistence.v2.queryGeneration.select.SelectFilteredQueryGenerator;
import kpersistence.v2.tables.StringIdTable;
import kpersistence.v2.tables.UserIdView;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

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
