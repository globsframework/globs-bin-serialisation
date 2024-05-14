package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.metamodel.fields.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.FieldReaderVisitorCreator;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.model.FieldNumber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobTypeFieldReadersManager implements GlobTypeFieldReadersManager {
    private final Map<String, GlobTypeFieldReaders> readersMap = new ConcurrentHashMap<>();

    public GlobTypeFieldReaders getOrCreate(GlobType globType) {
       return readersMap.computeIfAbsent(globType.getName(), s -> create(globType));
    }

    public GlobTypeFieldReaders create(GlobType globType) {
        Field[] fields = globType.getFields();
        List<FieldReader> fieldReaders = new ArrayList<>();

        for (Field field : fields) {
            field.findOptAnnotation(FieldNumber.key)
                    .map(FieldNumber.fieldNumber)
                    .ifPresent(fieldNumber -> field.safeVisit(
                            new FieldReaderVisitorCreator(fieldNumber, fieldReaders)
                    ));
        }

        Integer size = fieldReaders.stream()
                .map(FieldReader::getFieldNumber)
                .max(Integer::compareTo)
                .orElse(0);

        FieldReader[] readers = new FieldReader[size + 1];
        Arrays.fill(readers, UnknownFieldReader.INSTANCE);

        for (FieldReader fieldReader : fieldReaders) {
            readers[fieldReader.getFieldNumber()] = fieldReader;
        }
        return new GlobTypeFieldReaders(readers);
    }
}
