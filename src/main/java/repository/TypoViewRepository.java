package repository;

import repository.tables.TypoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public abstract class TypoViewRepository<T extends TypoView> extends AbstractRepository<T> {

    public TypoViewRepository(Class<T> clazz) {
        super(clazz);
    }

    @Autowired
    protected JdbcOperations jdbcOperations;

    @Autowired
    protected NamedParameterJdbcOperations namedParameterJdbcOperations;

}
