package habilitpro.services.usuario;

import habilitpro.model.dao.usuario.PerfilDAO;
import habilitpro.model.persistence.usuario.Perfil;
import habilitpro.model.persistence.usuario.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class PerfilService {
    private static final Logger LOG = LogManager.getLogger(PerfilService.class);
    private EntityManager entityManager;
    private PerfilDAO perfilDAO;

    public PerfilService(EntityManager entityManager, PerfilDAO perfilDAO) {
        this.entityManager = entityManager;
        this.perfilDAO = perfilDAO;
    }

    public void create(Perfil perfil) {
        LOG.info("Preparando para criar um perfil.");

        validateIfNull(perfil);

        String nome = perfil.getNome();
        if (nome == null || nome.isBlank()) {
            LOG.error("O nome do perfil está nulo!");
            throw new EntityNotFoundException("Nome nulo");
        }

        beginTransaction();
        perfilDAO.create(perfil);
        commitAndCloseTransaction();
        LOG.info("Perfil criado com sucesso!");
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID está nulo!");
            throw new EntityNotFoundException("ID nulo");
        }

        Perfil perfil = perfilDAO.getById(id);
        validateIfNull(perfil);

        beginTransaction();
        perfilDAO.delete(perfil);
        commitAndCloseTransaction();
        LOG.info("Perfil deletado com sucesso!");
    }

    public Perfil getById(Long id) {
        if (id == null) {
            LOG.error("O ID está nulo!");
            throw new EntityNotFoundException("ID nulo");
        }

        Perfil perfil = perfilDAO.getById(id);

        validateIfNull(perfil);

        LOG.info("Perfil encontrado!");
        return perfil;
    }

    public Perfil findByNome(String nome) {
        if (nome == null) {
            LOG.error("O nome do perfil está nulo!");
            throw new EntityNotFoundException("Nome nulo");
        }

        Perfil perfil = perfilDAO.findByNome(nome);
        validateIfNull(perfil);

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
        validateIfNull(perfil);

        if (usuario == null) {
            this.LOG.error("O usuário não existe!");
            throw new EntityNotFoundException("Usuário nulo");
        }

        LOG.info("Adicionando usuário.");

        beginTransaction();
        perfil.getUsuarios().add(usuario);
        commitAndCloseTransaction();
        LOG.info("Usuário adicionado com sucesso!");
    }

    public void removeUsuario(Perfil perfil, Usuario usuario) {
        validateIfNull(perfil);

        if (usuario == null) {
            this.LOG.error("O usuário não existe!");
            throw new EntityNotFoundException("Usuário nulo");
        }

        LOG.info("Removendo usuário.");

        beginTransaction();
        perfil.getUsuarios().remove(usuario);
        commitAndCloseTransaction();
        LOG.info("Usuário removido com sucesso!");
    }

    private void validateIfNull(Perfil perfil) {
        if (perfil == null) {
            LOG.error("O perfil não existe!");
            throw new EntityNotFoundException("Perfil nulo");
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
