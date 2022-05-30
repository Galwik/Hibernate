package connection;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Tworzy obiekt SessionFactory na podstawie konfiguracji z pliku hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable exception) {
            logger.error("Initial SessionFactory creation failed.", exception);
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Zamykanie cache i connection pool
        getSessionFactory().close();
    }
}