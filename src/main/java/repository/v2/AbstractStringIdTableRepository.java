package repository.v2;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractStringIdTableRepository<T extends StringIdTable> {

    Class<T> model;
    public static CurrentUserIdProvider currentUserIdProvider;
    RowMapper<T> allDataRowMapper;
    RowMapper<T> labelsRowMapper;

    @Autowired
    protected JdbcOperations jdbcOperations;

    @Autowired
    protected NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AbstractStringIdTableRepository() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelRepositoryMap.data.put(model, this);
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

    public String insert(T data) {
        String id = RandomId.next();
        UnnamedParametersQuery query = new InsertQueryGenerator(data, user(), id).generateInsertQuery();
        System.out.println(query);

        jdbcOperations.update(query.getQuery(), query.getParams());
        return id;
    }

    public void update(T data) {
        UnnamedParametersQuery query = new UpdateQueryGenerator(data, user()).generateInsertQuery();
        System.out.println(query);

        jdbcOperations.update(query.getQuery(), query.getParams());
    }

    public String delete(String id) {
        UnnamedParametersQuery query = new DeleteQueryGenerator(model, id, user()).generateDeleteQuery();
        System.out.println(query);

        jdbcOperations.update(query.getQuery(), query.getParams());

        return id;
    }
}
