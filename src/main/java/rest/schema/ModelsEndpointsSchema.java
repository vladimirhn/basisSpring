package rest.schema;

import kutils.PackageUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModelsEndpointsSchema {

    static Map<Class<?>, String> modelToEndpoint;

    private static void makeSchema(String controllersDir) {
        try {

            modelToEndpoint = new LinkedHashMap<>();

            PackageUtils.getClassesRecursively(controllersDir).stream()
                    .filter(klass -> klass.isAnnotationPresent(RequestMapping.class))
                    .forEach(controller -> {

                        if (controller.getAnnotation(RequestMapping.class).value().length > 0) {
                            String endPoint = controller.getAnnotation(RequestMapping.class).value()[0];
                            Type type = controller.getGenericSuperclass();

                            if (type instanceof ParameterizedType) {
                                ParameterizedType pType = (ParameterizedType) controller.getGenericSuperclass();

                                if (pType.getActualTypeArguments()[0] instanceof Class<?>) {
                                    Class<?> model = (Class<?>) pType.getActualTypeArguments()[0];
                                    modelToEndpoint.put(model, endPoint);
                                }

                            }
                        }
                    });

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Map<Class<?>, String> getSchema(String controllersDir) {
        if (modelToEndpoint == null) {
            ModelsEndpointsSchema.makeSchema(controllersDir);
        }
        return modelToEndpoint;
    }
}
