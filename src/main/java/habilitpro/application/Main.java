package habilitpro.application;

import habilitpro.connection.JpaConnectionFactory;
import habilitpro.model.enums.EnumRegionalEmpresa;
import habilitpro.model.enums.EnumSegmentoEmpresa;
import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;

import javax.persistence.EntityManager;

import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import habilitpro.model.persistence.modulo.Modulo;
import habilitpro.model.persistence.trabalhador.Funcao;
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

import java.time.OffsetDateTime;
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

        Empresa empresa = new Empresa("aBbA", "16.169.773/0001-36", EnumTipoEmpresa.MATRIZ, "d", EnumSegmentoEmpresa.ALIB, "Belém", "Pa", EnumRegionalEmpresa.VIM);
        Empresa empresa1 = new Empresa("ceb", "96644218000158", EnumTipoEmpresa.FILIAL, "d", EnumSegmentoEmpresa.ALIB, "aNANINDEUA", "pA", EnumRegionalEmpresa.VIM);
        //empresaService.create(empresa1);
        //empresaService.update(empresa1, 1L);
        //empresaService.delete(2L);
        //List<Empresa> empresas = empresaService.listByTipo(EnumTipoEmpresa.FILIAL);
        //List<Empresa> empresas = empresaService.listAll();
        //empresas.forEach(e -> System.out.println(e));

        //Setor setor = new Setor(empresaService.getById(1L), "Sector Z");
        //Setor setor1 = new Setor(empresaService.getById(1L), "Y");
        //setorService.create(setor1);

        //Funcao funcao = new Funcao("Afim", setorService.getById(2L));
        //funcaoService.update(funcao, 2L);
        //setorService.delete(1L);
        //empresaService.delete(1L);

        //Trabalhador trabalhador = new Trabalhador("farID GerMaNo", "787.826.180-62", empresaService.getById(2L), funcaoService.getById(1L));
        //Trabalhador trabalhador1 = new Trabalhador("casimiro miguel", "142.928.020-41", empresaService.getById(2L), funcaoService.getById(1L));
        //trabalhadorService.create(trabalhador1);
        //trabalhadorService.update(trabalhador, 1L);
        //funcaoService.delete(1L);

        //Ocupacao ocupacao = new Ocupacao("Japão, 1945-51");
        //ocupacaoService.create(ocupacao);

        //Trilha trilha = new Trilha(empresaService.getById(2L), ocupacaoService.getById(1L), 4, "vamos ver");
        //trilhaService.create(trilha);
        //ocupacaoService.delete(1L);

        //trilhaService.update(trilha, 3L);
        //trilhaService.addTrabalhador(trilhaService.getById(3L), trabalhadorService.getById(1L));

        //trilhaService.delete(3L);

        //Modulo modulo = new Modulo(trilhaService.getById(2L), "bob your head", "head bobbing", "keep doing it");
        //moduloService.create(modulo);
        //trilhaService.delete(2L);
        //moduloService.iniciarModulo(moduloService.getById(1L), OffsetDateTime.now());

        AvaliacaoModulo avaliacaoModulo = new AvaliacaoModulo(moduloService.getById(1L), trabalhadorService.getById(1L));
        avaliacaoModuloService.create(avaliacaoModulo);
    }
}
