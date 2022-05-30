package repository.v2;

import kcollections.CollectionFactory;
import kcollections.KList;
import kpersistence.v2.CurrentUserIdProvider;
import kpersistence.v2.RandomId;
import kpersistence.v2.UnnamedParametersQuery;
import kpersistence.v2.mapping.MapperByModel;
import kpersistence.v2.queryGeneration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import kpersistence.v2.tables.StringIdTable;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractStringIdTableRepository<T extends StringIdTable> {

    Class<T> model;
    public static CurrentUserIdProvider currentUserIdProvider;
    RowMapper<T> rowMapper;

    @Autowired
    protected JdbcOperations jdbcOperations;

    @Autowired
    protected NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AbstractStringIdTableRepository() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelRepositoryMap.data.put(model, this);
        rowMapper = new MapperByModel<T>(model)::mapRow;
    }

    private String user() {
        return currentUserIdProvider != null ? currentUserIdProvider.getCurrentUserId() : null;
    }

    public KList<T> selectAll() {
        UnnamedParametersQuery query = new SelectAllQueryGenerator(model, user()).generateSelectAllQuery();
        System.out.println(query);

        return CollectionFactory.makeListFrom(jdbcOperations::query, query.getQuery(), query.getParams(), rowMapper);
    }

    public KList<T> selectFiltered(T data) {
        UnnamedParametersQuery query = new SelectFilteredQueryGenerator(data, user()).generateSelectFilteredQuery();
        System.out.println(query);

//        return CollectionFactory.makeListFrom(jdbcOperations::query, query.getQuery(), query.getParams(), rowMapper);
        return null;
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
