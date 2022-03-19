package habilitpro.application;

import habilitpro.connection.JpaConnectionFactory;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    public static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();

        LOG.info("Boas vindas.");
    }
}
