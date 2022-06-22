package repository.v2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RepositoryInvocationHandler implements InvocationHandler {

    AbstractStringIdTableRepository repository;

    public RepositoryInvocationHandler(AbstractStringIdTableRepository repository) {
        this.repository = repository;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(repository, args);
    }
}
