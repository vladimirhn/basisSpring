package repository.mapping;

import kmodels.abstracts.K4StringsModel;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class K4StringsRowMapper<T extends K4StringsModel> implements RowMapper<T> {

    Class<T> type;

    public K4StringsRowMapper(Class<T> type) {
        this.type = type;
    }

//    public List<String> getColumns(ResultSet rs) {
//
//        List<String> result = new ArrayList<>(4);
//
//        try {
//
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columns = rsmd.getColumnCount();
//            for (int x = 1; x <= columns; x++) {
//                result.add(rsmd.getColumnName(x));
//            }
//        } catch (Exception ignored) {}
//
//        return result;
//    }

    @Override
    public T mapRow(ResultSet resultSet, int i) throws SQLException {

        try {
            T obj = type.getDeclaredConstructor().newInstance();

            obj.initData(
                    String.valueOf(resultSet.getObject(1)),
                    String.valueOf(resultSet.getObject(2)),
                    String.valueOf(resultSet.getObject(3)),
                    String.valueOf(resultSet.getObject(4))
            );

            return obj;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
