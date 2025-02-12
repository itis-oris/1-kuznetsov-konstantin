package su.intercraft.repository;

import org.flywaydb.core.Flyway;

public class FlywayConfig {
    static public void migrate() {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(DataSourceConfig.getDataSource())
                    .load();
            flyway.migrate();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
