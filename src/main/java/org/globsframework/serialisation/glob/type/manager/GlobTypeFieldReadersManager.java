package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldReaders;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldReadersFactory;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldReadersFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface GlobTypeFieldReadersManager {
    GlobTypeFieldReaders getOrCreate(GlobType globType);

    class Builder {
        private final GlobTypeFieldReadersFactory globTypeFieldReadersFactory;
        private final Map<GlobType, GlobTypeFieldReaders> containers = new HashMap<>();

        private Builder() {
            this.globTypeFieldReadersFactory = new DefaultGlobTypeFieldReadersFactory(containers);
        }

        public static Builder init() {
            return new Builder();
        }

        public Builder add(GlobType globType) {
            globTypeFieldReadersFactory.create(globType);
            return this;
        }

        public Builder add(Iterable<GlobType> types) {
            types.forEach(this::add);
            return this;
        }

        public GlobTypeFieldReadersManager build() {
            return new DefaultGlobTypeFieldReadersManager(new HashMap<>(containers), globTypeFieldReadersFactory);
        }
    }
}
