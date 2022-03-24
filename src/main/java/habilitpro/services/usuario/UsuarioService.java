package habilitpro.services.usuario;

import habilitpro.model.dao.usuario.UsuarioDAO;
import habilitpro.model.persistence.usuario.Perfil;
import habilitpro.model.persistence.usuario.Usuario;
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

        validateIfNull(usuario);

        String usuarioNome = usuario.getNome();
        if (usuarioNome == null || usuarioNome.isBlank()) {
            LOG.error("O nome do usuário está nulo!");
            throw new EntityNotFoundException("Nome nulo");
        }

        String usuarioCpf = usuario.getCpf();
        if (!checkCpf(usuarioCpf)) {
            LOG.error("O CPF do usuário é inválido!");
            throw new RuntimeException("CPF inválido");
        }

        String usuarioEmail = usuario.getEmail();
        if (!checkEmail(usuarioEmail)) {
            LOG.error("O e-mail do usuário é inválido!");
            throw new RuntimeException("E-mail inválido");
        }

        String usuarioSenha = usuario.getSenha();
        if (!checkSenha(usuarioSenha)) {
            LOG.error("A senha do usuário é inválida!");
            throw new RuntimeException("Senha inválida");
        }

        beginTransaction();
        usuarioDAO.create(usuario);
        commitAndCloseTransaction();
        LOG.info("Usuário criado com sucesso!");
    }

    private String formatCpf(String cpf) {
        return cpf.replaceAll("\\.|-","");
    }

    private boolean checkCpf(String cpf) {
        cpf = formatCpf(cpf);
        if (cpf.length() != 11 || cpf.equals("11111111111")
                || cpf.equals("22222222222")
                || cpf.equals("33333333333")
                || cpf.equals("44444444444")
                || cpf.equals("55555555555")
                || cpf.equals("66666666666")
                || cpf.equals("77777777777")
                || cpf.equals("88888888888")
                || cpf.equals("99999999999")
                || cpf.equals("00000000000")) {
            return false;
        }
        int digv1 = Character.getNumericValue(cpf.charAt(9));
        int digv2 = Character.getNumericValue(cpf.charAt(10));

        //verificar o primeiro dígito
        int multiplicador = 10;
        int soma = 0;
        int resto1;

        for (int i = 0; i < 9; ++i) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * multiplicador;
            --multiplicador;
        }

        if (((soma * 10) % 11) == 10) {
            resto1 = 0;
        } else {
            resto1 = ((soma * 10) % 11);
        }

        //verificar o segundo dígito
        multiplicador = 11;
        soma = 0;
        int resto2;

        for (int i = 0; i < 10; ++i) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * multiplicador;
            --multiplicador;
        }

        if (((soma * 10) % 11) == 10) {
            resto2 = 0;
        } else {
            resto2 = ((soma * 10) % 11);
        }

        return (digv1 == resto1 && digv2 == resto2);
    }

    public static boolean checkEmail(String email) {
        String rePattern = "(.+)@(.+)\\.(.+)";
        return email.matches(rePattern);
    }

    public static boolean checkSenha(String senha) {
        return senha.length() >= 8 && senha.matches(".*\\d.*") && senha.matches(".*\\w.*");
    }

    public boolean contains(Usuario usuario, Perfil perfil) {
        for (Perfil p : usuario.getPerfis()) {
            if (perfil.equals(p)) return true;
        }
        return false;
    }

    public void addPerfil(Usuario usuario, Perfil perfil) {
        validateIfNull(usuario);

        if (perfil == null) {
            LOG.error("O perfil não existe!");
            throw new EntityNotFoundException("Perfil nulo");
        }

        if (!contains(usuario, perfil)) {
            LOG.info("Adicionando perfil.");

            beginTransaction();
            usuario.getPerfis().add(perfil);
            perfilService.addUsuario(perfil, usuario);
            commitAndCloseTransaction();
            LOG.info("Perfil adicionado com sucesso!");
        }
    }

    public void removePerfil(Usuario usuario, Perfil perfil) {
        validateIfNull(usuario);

        if (perfil == null) {
            LOG.error("O perfil não existe!");
            throw new EntityNotFoundException("Perfil nulo");
        }

        if (contains(usuario, perfil)) {
            LOG.info("Removendo perfil.");

            beginTransaction();
            usuario.getPerfis().remove(perfil);
            perfilService.removeUsuario(perfil, usuario);
            commitAndCloseTransaction();
            LOG.info("Perfil removido com sucesso!");
        }
    }

    private void validateIfNull(Usuario usuario) {
        if (usuario == null) {
            LOG.error("O usuário não existe!");
            throw new EntityNotFoundException("Usuário nulo");
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
