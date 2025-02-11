package su.intercraft.listeners;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import su.intercraft.repository.DataSourceConfig;
import su.intercraft.repository.FlywayConfig;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FlywayConfig.migrate();
        System.out.println("Контекст сервлета инициализирован.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DataSourceConfig.closeConnection();
        System.out.println("Контекст сервлета уничтожен.");
    }
}