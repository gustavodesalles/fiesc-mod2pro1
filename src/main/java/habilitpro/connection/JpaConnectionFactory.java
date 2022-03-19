package habilitpro.connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaConnectionFactory {
    private EntityManagerFactory factory;

    public JpaConnectionFactory() {
        this.factory = Persistence.createEntityManagerFactory("habilitpro");
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
