package habilitpro.application;

import habilitpro.connection.JpaConnectionFactory;
import habilitpro.model.enums.EnumRegionalEmpresa;
import habilitpro.model.enums.EnumSegmentoEmpresa;
import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;

import javax.persistence.EntityManager;

import habilitpro.model.persistence.empresa.Setor;
import habilitpro.services.empresa.EmpresaService;
import habilitpro.services.empresa.SetorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Main {
    public static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
        EmpresaService empresaService = new EmpresaService(entityManager);
        SetorService setorService = new SetorService(entityManager);

        LOG.info("Boas vindas.");

        // Testes de empresa

        //Empresa empresa = new Empresa("aBbA", "16.169.773/0001-36", EnumTipoEmpresa.MATRIZ, "d", EnumSegmentoEmpresa.ALIB, "Belém", "Pa", EnumRegionalEmpresa.VIM);
        //Empresa empresa1 = new Empresa("b", "96644218000158", EnumTipoEmpresa.FILIAL, "d", EnumSegmentoEmpresa.ALIB, "aNANINDEUA", "pA", EnumRegionalEmpresa.VIM);
        //empresaService.create(empresa1);
        //empresaService.update(empresa, 5L);
        //empresaService.delete(4L);
        //List<Empresa> empresas = empresaService.listByTipo(EnumTipoEmpresa.FILIAL);
        //List<Empresa> empresas = empresaService.listAll();
        //empresas.stream().forEach(e -> System.out.println(e));
        //Ocupacao ocupacao = new Ocupacao("test");
        //Trilha trilha = new Trilha(empresa, ocupacao, 5, "", new ArrayList<Trabalhador>());

        // Testes de setor

        //Setor setor = new Setor(empresaService.getById(5L), "Departamento de apartamentos");
        //Setor setor = new Setor(empresaService.getById(6L), "Departamento de outras coisas");
        //setorService.create(setor);
        //setorService.update(setor, 2L);
        //List<Setor> setors = setorService.listByEmpresa(empresaService.getById(6L));
        //setors.stream().forEach(s -> System.out.println(s));
        empresaService.delete(6L);
    }
}
