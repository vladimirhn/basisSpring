package repository.v2;

import application.ContextProvider;
import kcollections.CollectionFactory;
import kcollections.KCollection;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    public <V> KList<T> selectByField(BiConsumer<T, V> fieldSetter, V fieldValue) {
        try {
            T instance = model.getDeclaredConstructor().newInstance();
            fieldSetter.accept(instance, fieldValue);

            return selectFiltered(instance);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return CollectionFactory.makeArrayList();
        }
    }

    public KList<T> customSelect(String sql, Object[] params) {
        System.out.println(sql);
        Arrays.stream(params).forEach(System.out::println);
        return CollectionFactory.makeListFrom(jdbcOperations::query, sql, params, allDataRowMapper);
    }
}
