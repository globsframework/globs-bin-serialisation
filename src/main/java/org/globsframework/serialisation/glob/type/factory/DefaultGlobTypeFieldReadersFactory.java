package org.globsframework.serialisation.glob.type.factory;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.serialisation.BinReader;
import org.globsframework.serialisation.field.FieldReader;
import org.globsframework.serialisation.field.reader.FieldReaderVisitorCreator;
import org.globsframework.serialisation.field.reader.UnknownFieldReader;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.model.FieldNumberAnnotationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultGlobTypeFieldReadersFactory implements GlobTypeFieldReadersFactory {
    private final BinReader binReader;

    public DefaultGlobTypeFieldReadersFactory(BinReader binReader) {
        this.binReader = binReader;
    }

    public GlobTypeFieldReaders create(GlobType globType) {
        Field[] fields = globType.getFields();
        List<FieldReader> fieldReaders = new ArrayList<>();

        for (Field field : fields) {
            field.findOptAnnotation(FieldNumberAnnotationType.key)
                    .map(FieldNumberAnnotationType.fieldNumber)
                    .ifPresent(fieldNumber -> field.safeVisit(
                            new FieldReaderVisitorCreator(binReader, fieldNumber, fieldReaders)
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
