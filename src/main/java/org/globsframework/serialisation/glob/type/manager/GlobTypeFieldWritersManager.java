package org.globsframework.serialisation.glob.type.manager;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.serialisation.glob.type.GlobTypeFieldWriters;
import org.globsframework.serialisation.glob.type.factory.DefaultGlobTypeFieldWritersFactory;
import org.globsframework.serialisation.glob.type.factory.GlobTypeFieldWritersFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface GlobTypeFieldWritersManager {
    GlobTypeFieldWriters getOrCreate(GlobType globType);

    class Builder {
        private final Map<GlobType, GlobTypeFieldWriters> containers = new HashMap<>();
        private final GlobTypeFieldWritersFactory globTypeFieldWritersFactory;

        private Builder() {
            this.globTypeFieldWritersFactory = new DefaultGlobTypeFieldWritersFactory(containers);
        }

        static public Builder init() {
            return new Builder();
        }

        public Builder add(GlobType globType) {
            globTypeFieldWritersFactory.create(globType);
            return this;
        }

        public Builder add(Iterable<GlobType> types) {
            types.forEach(this::add);
            return this;
        }

        public GlobTypeFieldWritersManager build() {
            return new DefaultGlobTypeFieldWritersManager(new HashMap<>(containers), globTypeFieldWritersFactory);
        }
    }

}
