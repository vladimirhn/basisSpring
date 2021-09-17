package repository;

import repository.tables.AbstractView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public abstract class AbstractViewRepository<T extends AbstractView> extends AbstractRepository<T> {

    public AbstractViewRepository(Class<T> clazz) {
        super(clazz);
    }

    @Autowired
    protected JdbcOperations jdbcOperations;

    @Autowired
    protected NamedParameterJdbcOperations namedParameterJdbcOperations;

}
