package habilitpro.services.usuario;

import habilitpro.model.dao.usuario.UsuarioDAO;
import habilitpro.model.persistence.usuario.Perfil;
import habilitpro.model.persistence.usuario.Usuario;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class UsuarioService {
    private static final Logger LOG = LogManager.getLogger(UsuarioService.class);
    private EntityManager entityManager;
    private UsuarioDAO usuarioDAO;
    private PerfilService perfilService;

    public UsuarioService(EntityManager entityManager, UsuarioDAO usuarioDAO, PerfilService perfilService) {
        this.entityManager = entityManager;
        this.usuarioDAO = usuarioDAO;
        this.perfilService = perfilService;
    }

    public void create(Usuario usuario) {
        LOG.info("Preparando para criar o usuário.");

        Validar.validarUsuario(usuario);

        String nome = usuario.getNome();
        Validar.validarString(nome);

        String cpf = usuario.getCpf();
        Validar.validarCpf(cpf);

        String email = usuario.getEmail();
        Validar.validarEmail(email);

        String senha = usuario.getSenha();
        Validar.validarSenha(senha);

        try {
            beginTransaction();
            usuarioDAO.create(usuario);
            commitAndCloseTransaction();
            LOG.info("Usuário criado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao criar o usuário, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        Validar.validarId(id);

        LOG.info("Preparando para encontrar o usuário.");

        Usuario usuario = getById(id);

        try {
            beginTransaction();
            usuarioDAO.delete(usuario);
            commitAndCloseTransaction();
            LOG.info("Usuário deletado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar o usuário, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Usuario novoUsuario, Long id) {
        if (novoUsuario == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new EntityNotFoundException("Parâmetro nulo");
        }

        Usuario usuario = getById(id);

        Validar.validarString(novoUsuario.getNome());
        Validar.validarCpf(novoUsuario.getCpf());
        Validar.validarEmail(novoUsuario.getEmail());
        Validar.validarSenha(novoUsuario.getSenha());

        try {
            beginTransaction();
            usuario.setNome(novoUsuario.getNome());
            usuario.setCpf(novoUsuario.getCpf());
            usuario.setEmail(novoUsuario.getEmail());
            usuario.setSenha(novoUsuario.getSenha());
            commitAndCloseTransaction();
        } catch (Exception e) {
            LOG.error("Erro ao atualizar o usuário, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Usuario getById(Long id) {
        Validar.validarId(id);

        Usuario usuario = usuarioDAO.getById(id);
        Validar.validarUsuario(usuario);

        LOG.info("Usuário encontrado!");
        return usuario;
    }

    public Usuario getByEmailAndSenha(String email, String senha) {
        Validar.validarEmail(email);
        Validar.validarSenha(senha);

        Usuario usuario = usuarioDAO.getByEmailAndSenha(email, senha);

        if (usuario != null) {
            LOG.info("Usuário encontrado!");
            return usuario;
        } else {
            LOG.info("Usuário não encontrado!");
            return null;
        }
    }

    public boolean contains(Usuario usuario, Perfil perfil) {
        Validar.validarUsuario(usuario);

        Validar.validarPerfil(perfil);

        for (Perfil p : usuario.getPerfis()) {
            if (perfil.equals(p)) return true;
        }
        return false;
    }

    public void addPerfil(Usuario usuario, Perfil perfil) {
        Validar.validarUsuario(usuario);

        Validar.validarPerfil(perfil);

        if (!contains(usuario, perfil)) {
            LOG.info("Adicionando perfil.");

            try {
                beginTransaction();
                usuario.getPerfis().add(perfil);
                perfil.getUsuarios().add(usuario);
                commitAndCloseTransaction();
                LOG.info("Perfil adicionado com sucesso!");
            } catch (Exception e) {
                LOG.error("Erro ao adicionar o perfil, causado por: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void removePerfil(Usuario usuario, Perfil perfil) {
        Validar.validarUsuario(usuario);

        Validar.validarPerfil(perfil);

        if (contains(usuario, perfil)) {
            LOG.info("Removendo perfil.");

            try {
                beginTransaction();
                usuario.getPerfis().remove(perfil);
                perfilService.removeUsuario(perfil, usuario);
                commitAndCloseTransaction();
                LOG.info("Perfil removido com sucesso!");
            } catch (Exception e) {
                LOG.error("Erro ao remover o perfil, causado por: " + e.getMessage());
                throw new RuntimeException(e);
            }
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
