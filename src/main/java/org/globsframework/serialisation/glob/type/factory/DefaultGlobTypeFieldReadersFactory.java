package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.FieldReaderVisitorCreator;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.model.FieldNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultGlobTypeFieldReadersFactory implements GlobTypeFieldReadersFactory {

    public GlobTypeFieldReaders create(GlobType globType) {
        Field[] fields = globType.getFields();
        List<FieldReader> fieldReaders = new ArrayList<>();

        for (Field field : fields) {
            field.findOptAnnotation(FieldNumber.KEY)
                    .map(FieldNumber.fieldNumber)
                    .ifPresent(fieldNumber -> field.safeAccept(
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
