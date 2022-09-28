package repository.v2;

import kpersistence.v2.RandomId;
import kpersistence.v2.UnnamedParametersQuery;
import kpersistence.v2.mapping.MapperAllDataByModel;
import kpersistence.v2.mapping.MapperLabelsByModel;
import kpersistence.v2.queryGeneration.change.DeleteQueryGenerator;
import kpersistence.v2.queryGeneration.change.DeleteSimilarQueryGenerator;
import kpersistence.v2.queryGeneration.change.InsertQueryGenerator;
import kpersistence.v2.queryGeneration.change.UpdateQueryGenerator;
import kpersistence.v2.tables.StringIdTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.function.BiConsumer;

public class AbstractStringIdTableRepository<T extends StringIdTable> extends AbstractRepository<T> {


    public AbstractStringIdTableRepository() {
        model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        ModelRepositoryMap.data.put(model, this);
        allDataRowMapper = new MapperAllDataByModel<>(model)::mapRow;
        labelsRowMapper = new MapperLabelsByModel<>(model)::mapRow;
    }
    public AbstractStringIdTableRepository(Class<T> model) {
        super(model);
    }

    public T selectOne(String id) throws Exception {
        T example = model.getConstructor().newInstance();
        example.setId(id);

        return selectFiltered(example).getFirst().orElseThrow();
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

    public String deleteSimilar(T data) {
        UnnamedParametersQuery query = new DeleteSimilarQueryGenerator<>(model, data, user()).generateDeleteQuery();
        System.out.println(query);

        jdbcOperations.update(query.getQuery(), query.getParams());

        return data.getId();
    }

    public <V> String deleteByField(BiConsumer<T, V> fieldSetter, V fieldValue) {
        try {
            T instance = model.getDeclaredConstructor().newInstance();
            fieldSetter.accept(instance, fieldValue);

            return deleteSimilar(instance);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
