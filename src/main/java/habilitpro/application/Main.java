package habilitpro.application;

import habilitpro.connection.JpaConnectionFactory;
import habilitpro.model.enums.EnumRegionalEmpresa;
import habilitpro.model.enums.EnumSegmentoEmpresa;
import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;

import javax.persistence.EntityManager;

import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import habilitpro.model.persistence.trilha.Ocupacao;
import habilitpro.model.persistence.trilha.Trilha;
import habilitpro.services.empresa.EmpresaService;
import habilitpro.services.empresa.SetorService;
import habilitpro.services.modulo.AvaliacaoModuloService;
import habilitpro.services.modulo.ModuloService;
import habilitpro.services.trabalhador.FuncaoService;
import habilitpro.services.trabalhador.TrabalhadorService;
import habilitpro.services.trilha.OcupacaoService;
import habilitpro.services.trilha.TrilhaService;
import habilitpro.services.usuario.PerfilService;
import habilitpro.services.usuario.UsuarioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
        EmpresaService empresaService = new EmpresaService(entityManager);
        SetorService setorService = new SetorService(entityManager);
        TrilhaService trilhaService = new TrilhaService(entityManager);
        OcupacaoService ocupacaoService = new OcupacaoService(entityManager);
        ModuloService moduloService = new ModuloService(entityManager);
        AvaliacaoModuloService avaliacaoModuloService = new AvaliacaoModuloService(entityManager);
        TrabalhadorService trabalhadorService = new TrabalhadorService(entityManager);
        FuncaoService funcaoService = new FuncaoService(entityManager);
        UsuarioService usuarioService = new UsuarioService(entityManager);
        PerfilService perfilService = new PerfilService(entityManager);

        LOG.info("Boas vindas.");

        // Testes de empresa

        Empresa empresa = new Empresa("aBbA", "16.169.773/0001-36", EnumTipoEmpresa.MATRIZ, "d", EnumSegmentoEmpresa.ALIB, "Belém", "Pa", EnumRegionalEmpresa.VIM);
        Empresa empresa1 = new Empresa("ceb", "96644218000158", EnumTipoEmpresa.FILIAL, "d", EnumSegmentoEmpresa.ALIB, "aNANINDEUA", "pA", EnumRegionalEmpresa.VIM);
        //empresaService.create(empresa);
        //empresaService.update(empresa, 5L);
        //empresaService.delete(4L);
        //List<Empresa> empresas = empresaService.listByTipo(EnumTipoEmpresa.FILIAL);
        //List<Empresa> empresas = empresaService.listAll();
        //empresas.stream().forEach(e -> System.out.println(e));
        //Empresa empresa = empresaService.getById(7L);
        //Ocupacao ocupacao = new Ocupacao("test");
        //ocupacaoService.create(ocupacao);

        //Trilha trilha = new Trilha(empresa, ocupacao, 4, "oh no");
        //trilhaService.create(trilha);
        //empresaService.delete(7L);
        //trilhaService.delete(3L);
        //ocupacaoService.delete(2L);

        // Testes de setor

        //Setor setor = new Setor(empresaService.getById(5L), "Departamento de apartamentos");
        //Setor setor = new Setor(empresaService.getById(6L), "Departamento de outras coisas");
        //setorService.create(setor);
        //setorService.update(setor, 2L);
        //setorService.delete(2L);
        //List<Setor> setors = setorService.listByEmpresa(empresaService.getById(6L));
        //setors.stream().forEach(s -> System.out.println(s));
        empresaService.delete(5L);
    }
}
