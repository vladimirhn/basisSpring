package repository;

import kcollections.CollectionFactory;
import kcollections.KList;
import koptional.KOptional;
import kpersistence.QueryGenerator;
import kpersistence.UnnamedParametersQuery;
import kpersistence.kfilters.*;
import repository.mapping.KRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractRepository<T> {

    protected Class<T> modelClass;
    protected RowMapper<T> rowMapper;

    public AbstractRepository(Class<T> clazz) {
        modelClass = clazz;
        rowMapper = new KRowMapper<>(clazz);
    }

    @Autowired
    protected JdbcOperations jdbcOperations;

    @Autowired
    protected NamedParameterJdbcOperations namedParameterJdbcOperations;

    public KOptional<T> selectFirst(T obj) {
        UnnamedParametersQuery selectQuery = QueryGenerator.generateSelectSimilarQuery(obj);
        KList<T> res = CollectionFactory.makeListFrom(jdbcOperations::query, selectQuery.getQuery(), selectQuery.getParams(), rowMapper);
        return res.getFirst();
    }

    public KOptional<T> findOne(String id) {
        UnnamedParametersQuery selectQuery = QueryGenerator.generateSelectOneQuery(id, modelClass);
        KList<T> res = CollectionFactory.makeListFrom(jdbcOperations::query, selectQuery.getQuery(), selectQuery.getParams(), rowMapper);
        return res.getFirst();
    }

    public KList<T> selectAll() {
        String sql = QueryGenerator.generateSelectAllQuery(modelClass);

        System.out.println(sql);

        return CollectionFactory.makeListFrom(jdbcOperations::query, sql, rowMapper);
    }

    public Stream<T> streamAll() {
        return selectAll().stream();
    }

    public KList<T> selectSimilar(T obj) {
        UnnamedParametersQuery selectQuery = QueryGenerator.generateSelectSimilarQuery(obj);

        System.out.println(selectQuery.getQuery());

        KList<T> res = CollectionFactory.makeList(jdbcOperations.query(selectQuery.getQuery(), selectQuery.getParams(), rowMapper));

        return res;
    }

    public KList<T> selectWithQuery(String sql) {
        return CollectionFactory.makeListFrom(jdbcOperations::query, sql, rowMapper);
    }

    public <V> KList<T> selectByField(BiConsumer<T, V> fieldSetter, V fieldValue) {
        try {
            T instance = modelClass.getDeclaredConstructor().newInstance();
            fieldSetter.accept(instance, fieldValue);

            return selectSimilar(instance);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return CollectionFactory.makeLinkedList();
        }
    }

    //KFilters
    public List<T> search(Filter<?,?>... filters) {
        List<T> result = new ArrayList<>();

        getPartedFilters(filters).forEach(filtersSet -> {

            QueryComposer<T> composer = new QueryComposer<>(this.modelClass, filtersSet.toArray(new Filter[0]));

            String sql = composer.getSelectSql();
            result.addAll(executeSelect(composer, sql, filters));

        });

        return result;
    }

    public List<T> search(int limit, Filter<?,?>... filters) {
        List<T> result = new ArrayList<>();

        getPartedFilters(filters).forEach(filtersSet -> {

            QueryComposer<T> composer = new QueryComposer<>(this.modelClass, filtersSet.toArray(new Filter[0]));

            String sql = composer.getSelectSql(limit);
            result.addAll(executeSelect(composer, sql, filters));

        });

        return result;
    }

    public List<T> search(int limit, int offset, Filter<?,?>... filters) {
        List<T> result = new ArrayList<>();

        getPartedFilters(filters).forEach(filtersSet -> {

            QueryComposer<T> composer = new QueryComposer<>(this.modelClass, filtersSet.toArray(new Filter[0]));

            String sql = composer.getSelectSql(limit, offset);
            result.addAll(executeSelect(composer, sql, filters));

        });

        return result;
    }

    public <O> List<T> search(int limit, int offset, Class<O> orderByFieldType, BiConsumer<T, O> orderByFieldSetter, Filter<?,?>... filters) {
        List<T> result = new ArrayList<>();

        getPartedFilters(filters).forEach(filtersSet -> {

            QueryComposer<T> composer = new QueryComposer<>(this.modelClass, filtersSet.toArray(new Filter[0]));

            String sql = composer.getSelectSql(limit, offset, orderByFieldType, orderByFieldSetter);
            result.addAll(executeSelect(composer, sql, filters));

        });

        return result;
    }

    private List<T> executeSelect(QueryComposer<T> composer, String sql, Filter<?,?>[] filters) {

        Map<String, Object> params = composer.getParams();
        String formattedParams = params.toString().replace(", p", "\np").replace("{", "").replace("}", "").replace("=", ": ");

        String log = sql + "\n" + formattedParams;
        System.out.println(log);

        return CollectionFactory.makeListFrom(namedParameterJdbcOperations::query, sql.replace("\n", " "), params, rowMapper);
    }

    private List<List<Filter>> getPartedFilters(Filter[] filters) {

        List<Filter> initialFilterList = Arrays.stream(filters).collect(Collectors.toList());
        List<List<Filter>> result = new ArrayList<>();

        List<Filter> toBeParted = Arrays.stream(filters)
                .filter(f -> f != null && f.getSqlOperator() == SqlOperator.IN && f.getValuesArray().length > 1000)
                .collect(Collectors.toList());

        if (toBeParted.size() > 1) {
            throw new IllegalArgumentException("We cannot process more than one Filter with IN operator and 1000+ parameters.");

        } else if (toBeParted.size() < 1) {
            result.add(initialFilterList);

        } else if (toBeParted.size() == 1) {
            Filter longInFilter = toBeParted.get(0);
            initialFilterList.remove(longInFilter);

            CollectionFactory.makeList(longInFilter.getValuesArray()).split(1000).forEach(params -> {
                List<Filter> currList = new ArrayList<>(initialFilterList);
                Filter parted = new Filter<>(longInFilter, params.toArray());
                currList.add(parted);

                result.add(currList);
            });
        }

        return result;
    }


    public int countByFilters(Filter... filters) {

        int sum = 0;
        for (List<Filter> filtersSet : getPartedFilters(filters)) {

            QueryComposer<T> composer = new QueryComposer<T>(this.modelClass, filtersSet.toArray(new Filter[0]));

            String sql = composer.getCountSql();
            Map<String, Object> params = composer.getParams();

            String formattedParams = params.toString().replace(", p", "\np").replace("{", "").replace("}", "").replace("=", ": ");

            String log = sql + "\n" + formattedParams;
            System.out.println(log);

            Integer res = namedParameterJdbcOperations.queryForObject(sql.replace("\n", " "), params, Integer.class);
            sum += res == null ? 0 : res;
        }
        return sum;
    }

    public <R extends Number> R sumByFilters(Class<R> type, BiConsumer<T, R> sumField, Filter... filters) {

        BigDecimal sum = BigDecimal.ZERO;
        for (List<Filter> filtersSet : getPartedFilters(filters)) {

            QueryComposer<T> composer =
                    new QueryComposer<T>(this.modelClass, filtersSet.toArray(new Filter[0]));

            String sql = composer.getSumSql(type, sumField);
            Map<String, Object> params = composer.getParams();

            String formattedParams = params.toString().replace(", p", "\np").replace("{", "").replace("}", "").replace("=", ": ");

            String log = sql + "\n" + formattedParams;
            System.out.println(log);
            
            R res = namedParameterJdbcOperations.queryForObject(sql.replace("\n", " "), params, type);
            if (res != null) {
                sum = sum.add(BigDecimal.valueOf(res.doubleValue()));
            }
        }

        try {
            R result = null;

            if (type.equals(Double.class)) {
                result = type.getDeclaredConstructor(double.class).newInstance(sum.doubleValue());
            } else if (type.equals(Float.class)) {
                result = type.getDeclaredConstructor(float.class).newInstance(sum.floatValue());
            } else if (type.equals(Long.class)) {
                result = type.getDeclaredConstructor(long.class).newInstance(sum.longValue());
            } else if (type.equals(Integer.class)) {
                result = type.getDeclaredConstructor(int.class).newInstance(sum.intValue());
            } else {
                return null;
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public <V> String getColumnBySetter(BiConsumer<T, V> setter, Class<V> type) {
        return new SetterToColumnMapper<T>().map(this.modelClass, setter, type);
    }
    
    @SafeVarargs
    public final <V> List<String> getColumnBySetter(FieldRef<T, V>... setters) {
        return new SetterToColumnMapper<T>().map(this.modelClass, setters);
    }
    
    public <V> KList<V> searchDistinct(Class<V> fieldType, BiConsumer<T, V> fieldSetter, Filter... filters) {
        if (filters.length == 0) {
            filters = new Filter[]{new Filter<>(fieldSetter, SqlOperator.IS_NOT_NULL, fieldType)};
        }
        QueryComposer<T> composer = new QueryComposer<>(this.modelClass, filters);
        String sql = composer.getSelectDistinctSql(fieldType, fieldSetter);

        return CollectionFactory.makeList(jdbcOperations.queryForList(sql, fieldType));
    }
}
