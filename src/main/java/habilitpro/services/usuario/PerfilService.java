package habilitpro.services.usuario;

import habilitpro.model.dao.usuario.PerfilDAO;
import habilitpro.model.dao.usuario.UsuarioDAO;
import habilitpro.model.persistence.usuario.Perfil;
import habilitpro.model.persistence.usuario.Usuario;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class PerfilService {
    private static final Logger LOG = LogManager.getLogger(PerfilService.class);
    private EntityManager entityManager;
    private PerfilDAO perfilDAO;
    private UsuarioService usuarioService;

    public PerfilService(EntityManager entityManager, PerfilDAO perfilDAO, UsuarioService usuarioService) {
        this.entityManager = entityManager;
        this.perfilDAO = perfilDAO;
        this.usuarioService = usuarioService;
    }

    public void create(Perfil perfil) {
        LOG.info("Preparando para criar um perfil.");

        Validar.validarPerfil(perfil);

        String nome = perfil.getNome();
        Validar.validarString(nome);

        try {
            beginTransaction();
            perfilDAO.create(perfil);
            commitAndCloseTransaction();
            LOG.info("Perfil criado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao criar o perfil, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        Validar.validarId(id);

        Perfil perfil = perfilDAO.getById(id);
        Validar.validarPerfil(perfil);

        for (Usuario u : perfil.getUsuarios()) {
            perfil.getUsuarios().remove(perfil);
        }

        try {
            beginTransaction();
            perfilDAO.delete(perfil);
            commitAndCloseTransaction();
            LOG.info("Perfil deletado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar o perfil, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Perfil getById(Long id) {
        Validar.validarId(id);

        Perfil perfil = perfilDAO.getById(id);

        Validar.validarPerfil(perfil);

        LOG.info("Perfil encontrado!");
        return perfil;
    }

    public Perfil findByNome(String nome) {
        Validar.validarString(nome);

        Perfil perfil = perfilDAO.findByNome(nome);
        Validar.validarPerfil(perfil);

        LOG.info("Perfil encontrado!");
        return perfil;
    }

    public List<Perfil> listAll() {
        LOG.info("Preparando para listar os perfis.");
        List<Perfil> perfis = perfilDAO.listAll();

        if (perfis == null) {
            LOG.error("Não foram encontrados perfis!");
            throw new EntityNotFoundException("Não há perfis");
        }

        LOG.info("Número de perfis encontrados: " + perfis.size());
        return perfis;
    }

    public void addUsuario(Perfil perfil, Usuario usuario) {
        Validar.validarPerfil(perfil);

        Validar.validarUsuario(usuario);

        LOG.info("Adicionando usuário.");

        try {
            beginTransaction();
            perfil.getUsuarios().add(usuario);
            usuario.getPerfis().add(perfil);
            commitAndCloseTransaction();
            LOG.info("Usuário adicionado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao adicionar o usuário, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public void removeUsuario(Perfil perfil, Usuario usuario) {
        Validar.validarPerfil(perfil);

        Validar.validarUsuario(usuario);

        LOG.info("Removendo usuário.");

        try {
            beginTransaction();
            perfil.getUsuarios().remove(usuario);
            usuario.getPerfis().remove(perfil);
            commitAndCloseTransaction();
            LOG.info("Usuário removido com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao remover o usuário, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
