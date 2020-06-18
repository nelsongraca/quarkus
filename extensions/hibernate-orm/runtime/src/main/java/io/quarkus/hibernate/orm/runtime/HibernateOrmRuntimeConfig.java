package io.quarkus.hibernate.orm.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public class HibernateOrmRuntimeConfig {

    /**
     * Database related configuration.
     */
    @ConfigItem(name = "database")
    public HibernateOrmConfigDatabase database;

    @ConfigGroup
    public static class HibernateOrmConfigDatabase {

        /**
         * The default catalog to use for the database objects.
         */
        @ConfigItem(name = "default-catalog")
        public Optional<String> defaultCatalog;

        /**
         * The default schema to use for the database objects.
         */
        @ConfigItem
        public Optional<String> defaultSchema;

        public boolean isAnyPropertySet() {
            return defaultCatalog.isPresent()
                    || defaultSchema.isPresent();
        }
    }
}
