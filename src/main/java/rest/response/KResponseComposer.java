package rest.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.tables.MainTable;
import repository.tables.StringIdTable;
import rest.dictionary.DictionaryService;

import java.util.Collection;

@Service("responseComposer")
public class KResponseComposer {

    @Autowired
    DictionaryService dictionaryService;

    public SimpleTableResponse createFrom(Collection<?> data, Class<?> type) {

        if (StringIdTable.class.isAssignableFrom(type)) {

            if (MainTable.class.isAssignableFrom(type)) {

            } else {
                return new SimpleTableResponse(data, type, dictionaryService);
            }
        }

        return null;
    }
}