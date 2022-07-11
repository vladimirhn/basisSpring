package repository.v2;

import application.ContextProvider;
import kcollections.CollectionFactory;
import kcollections.KList;
import kpersistence.v2.CurrentUserIdProvider;
import kpersistence.v2.UnnamedParametersQuery;
import kpersistence.v2.mapping.MapperAllDataByModel;
import kpersistence.v2.mapping.MapperLabelsByModel;
import kpersistence.v2.modelsMaster.ModelsMaster;
import kpersistence.v2.queryGeneration.select.SelectAllQueryGenerator;
import kpersistence.v2.queryGeneration.select.SelectFilteredQueryGenerator;
import kpersistence.v2.tables.Table;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public abstract class AbstractRepository <T extends Table> {

    Class<T> model;
    public static CurrentUserIdProvider currentUserIdProvider;
    RowMapper<T> allDataRowMapper;
    RowMapper<T> labelsRowMapper;

    protected JdbcOperations jdbcOperations = ContextProvider.getContext().getBean(JdbcOperations.class);
    protected NamedParameterJdbcOperations namedParameterJdbcOperations = ContextProvider.getContext().getBean(NamedParameterJdbcOperations.class);

    public AbstractRepository() {
    }

    public AbstractRepository(Class<T> model) {
        this.model = model;
        allDataRowMapper = new MapperAllDataByModel<>(model)::mapRow;
        labelsRowMapper = new MapperLabelsByModel<>(model)::mapRow;
    }

    protected String user() {
        return currentUserIdProvider != null ? currentUserIdProvider.getCurrentUserId() : null;
    }

    public KList<T> selectAll(String orderByFieldName, String direction) {
        UnnamedParametersQuery query = new SelectAllQueryGenerator(ModelsMaster.getQueryAllDataModel(model), user(), orderByFieldName, direction).generateSelectAllQuery();
        System.out.println(query);

        return CollectionFactory.makeListFrom(jdbcOperations::query, query.getQuery(), query.getParams(), allDataRowMapper);
    }

    public KList<T> selectAllLabels(String orderByFieldName, String direction) {
        UnnamedParametersQuery query = new SelectAllQueryGenerator(ModelsMaster.getQueryLabelsModel(model), user(), orderByFieldName, direction).generateSelectAllQuery();
        System.out.println(query);

        return CollectionFactory.makeListFrom(jdbcOperations::query, query.getQuery(), query.getParams(), labelsRowMapper);
    }

    public KList<T> selectFiltered(T data) {
        UnnamedParametersQuery query = new SelectFilteredQueryGenerator(ModelsMaster.getQueryAllDataModel(model), data, user()).generateSelectFilteredQuery();
        System.out.println(query);

        return CollectionFactory.makeListFrom(jdbcOperations::query, query.getQuery(), query.getParams(), allDataRowMapper);
    }

    public KList<T> selectFilteredLabels(T data) {
        UnnamedParametersQuery query = new SelectFilteredQueryGenerator(ModelsMaster.getQueryLabelsModel(model), data, user()).generateSelectFilteredQuery();
        System.out.println(query);

        return CollectionFactory.makeListFrom(jdbcOperations::query, query.getQuery(), query.getParams(), labelsRowMapper);
    }
}
