package application;

public interface ServiceUser {

    static <T> T getBean(Class<T> beanClass) {
        return ContextProvider.getContext().getBean(beanClass);
    }
}
