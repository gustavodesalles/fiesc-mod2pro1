package habilitpro.application;

import habilitpro.connection.JpaConnectionFactory;
import habilitpro.model.enums.EnumRegionalEmpresa;
import habilitpro.model.enums.EnumSegmentoEmpresa;
import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;

import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    public static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();

        LOG.info("Boas vindas.");
        Empresa empresa = new Empresa("a", "16.169.773/0001-36", EnumTipoEmpresa.MATRIZ, "d", EnumSegmentoEmpresa.ALIB, "Belém", "Pará", EnumRegionalEmpresa.VIM);
        //Ocupacao ocupacao = new Ocupacao("test");
        //Trilha trilha = new Trilha(empresa, ocupacao, 5, "", new ArrayList<Trabalhador>());
    }
}
