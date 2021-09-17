package repository.mapping;

import kpersistence.mapping.annotations.Column;
import kpersistence.mapping.annotations.Foreign;
import kpersistence.mapping.annotations.Label;
import kpersistence.mapping.annotations.Table;
import kutils.ClassUtils;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class KRowMapper<T> implements RowMapper<T> {

    Class<T> type;

    public KRowMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T mapRow(ResultSet rs, int i) throws SQLException {
        try {
            T obj = type.getDeclaredConstructor().newInstance();
            List<Field> fields = ClassUtils.getFieldsUpToObject(type);

            for (Field field : fields) {

                field.setAccessible(true);

                if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(Foreign.class)) {

                    String colName = null;

                    if (field.isAnnotationPresent(Column.class)) {

                        colName = field.getAnnotation(Column.class).name();

                    } else if (field.isAnnotationPresent(Foreign.class)) {

                        Class<?> foreignTableClass = field.getAnnotation(Foreign.class).table();
                        String foreignTableName = foreignTableClass.getAnnotation(Table.class).name();
                        String foreignColumnName = ClassUtils
                                .getFieldsByAnnotation(foreignTableClass, Label.class).get(0)
                                .getAnnotation(Column.class).name();

                        colName = foreignTableName + "_" + foreignColumnName;
                    }

                    Object data = rs.getObject(colName);

                    Class<?> fieldType = field.getType();
                    Class<?> dataType = data != null ? data.getClass() : Object.class;

                    if (fieldType.equals(LocalDate.class) && dataType.equals(String.class)) {
                        field.set(obj, LocalDate.parse(data.toString()));
                    }

                    if (fieldType.equals(LocalDateTime.class) && dataType.equals(String.class)) {
                        field.set(obj, LocalDateTime.parse(data.toString()));
                    }

                    if (fieldType.equals(Long.class) && dataType.equals(Integer.class)) {
                        field.set(obj, (long) (int) data);
                    }

                    if (fieldType.equals(BigDecimal.class)) {
                        if (data != null) {
                            field.set(obj, new BigDecimal(String.valueOf(data)));
                        }
                    }

                    if (fieldType.equals(Enum.class)) {
                        if (data != null) {
                            Enum<?> enumObject = (Enum<?>)field.get(obj);
                            Class<? extends Enum> enumType = enumObject.getClass();
                            Enum<?> value = Enum.valueOf(enumType, data.toString());
                            field.set(obj, value);
                        }
                    }

                    //Sqlite boolean processing
                    if (fieldType.equals(Boolean.class)) {
                        if ("1".equals(String.valueOf(data))) {
                            field.set(obj, true);
                        }
                        if ("0".equals(String.valueOf(data))) {
                            field.set(obj, false);
                        }
                    }

                    //default
                    if (fieldType.isAssignableFrom(dataType)) {
                        field.set(obj, data);
                    }
                }

            }

            return obj;

        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("Не удаётся обработать модель. У модели обязан быть конструктор без параметров.");

        } catch (SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();

            throw new IllegalArgumentException("Не удаётся обработать модель. См. детали в логе.");
        }
    }
}
