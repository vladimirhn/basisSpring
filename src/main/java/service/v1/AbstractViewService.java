package service.v1;

import repository.v1.tables.AbstractView;
import repository.v1.AbstractViewRepository;
import kcollections.KList;
import koptional.KOptional;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

public abstract class AbstractViewService<T extends AbstractView> {

    protected abstract AbstractViewRepository<T> getRepository();

    //Main functionality
    //CHECKS
    public KOptional<T> selectFirst(T obj) {
        return getRepository().selectFirst(obj);
    }

    //SELECT
    public KOptional<T> findOne(String id) {
        return getRepository().findOne(id);
    }

    public KList<T> selectAll() {
        return getRepository().selectAll();
    }

    public Stream<T> streamAll() {
        return getRepository().streamAll();
    }

    public KList<T> findWithQuery(String sql) {
        return getRepository().selectWithQuery(sql);
    }

    public <V> KList<T> selectByField(BiConsumer<T, V> fieldSetter, V fieldValue) {
        return getRepository().selectByField(fieldSetter, fieldValue);
    }
}
