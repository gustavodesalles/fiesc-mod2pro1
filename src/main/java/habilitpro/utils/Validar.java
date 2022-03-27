package habilitpro.utils;

import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import habilitpro.model.persistence.modulo.Modulo;
import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import habilitpro.model.persistence.trilha.Ocupacao;
import habilitpro.model.persistence.trilha.Trilha;
import habilitpro.model.persistence.usuario.Perfil;
import habilitpro.model.persistence.usuario.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityNotFoundException;

public class Validar {
    private static final Logger LOG = LogManager.getLogger(Validar.class);

    public static void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            LOG.error("A empresa está nula!");
            throw new EntityNotFoundException("Empresa nula");
        }
    }

    public static void validarTrilha(Trilha trilha) {
        if (trilha == null) {
            LOG.error("A trilha está nula!");
            throw new EntityNotFoundException("Trilha nula");
        }
    }

    public static void validarModulo(Modulo modulo) {
        if (modulo == null) {
            LOG.error("O módulo está nulo!");
            throw new EntityNotFoundException("Módulo nulo");
        }
    }

    public static void validarAvaliacao(AvaliacaoModulo avaliacaoModulo) {
        if (avaliacaoModulo == null) {
            LOG.error("A avaliação está nula!");
            throw new EntityNotFoundException("Avaliação nula");
        }
    }

    public static void validarTrabalhador(Trabalhador trabalhador) {
        if (trabalhador == null) {
            LOG.error("O trabalhador está nulo!");
            throw new EntityNotFoundException("Trabalhador nulo");
        }
    }

    public static void validarSetor(Setor setor) {
        if (setor == null) {
            LOG.error("O setor está nulo!");
            throw new EntityNotFoundException("Setor nulo");
        }
    }

    public static void validarFuncao(Funcao funcao) {
        if (funcao == null) {
            LOG.error("A função está nula!");
            throw new EntityNotFoundException("Função nula");
        }
    }

    public static void validarOcupacao(Ocupacao ocupacao) {
        if (ocupacao == null) {
            LOG.error("A ocupação está nula!");
            throw new EntityNotFoundException("Ocupação nula");
        }
    }

    public static void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            LOG.error("O usuário não existe!");
            throw new EntityNotFoundException("Usuário nulo");
        }
    }

    public static void validarPerfil(Perfil perfil) {
        if (perfil == null) {
            LOG.error("O perfil não existe!");
            throw new EntityNotFoundException("Perfil nulo");
        }
    }

    public static void validarString(String string) {
        if (string == null || string.isBlank()) {
            LOG.error("A string está nula!");
            throw new RuntimeException("String nula");
        }
    }

    public static void validarId(Long id) {
        if (id == null) {
            LOG.error("O ID da trilha está nulo!");
            throw new RuntimeException("ID nulo");
        }
    }

    public static void validarSatisfacao(int satisfacao) {
        if (satisfacao > 5 || satisfacao < 1) {
            LOG.error("O nível de satisfação não é válido!");
            throw new RuntimeException("Nível de satisfação inválido");
        }
    }

    public static void validarCpf(String cpf) {
        if (!checkCpf(cpf)) {
            LOG.error("O CPF do trabalhador é inválido!");
            throw new RuntimeException("CPF inválido");
        }
    }

    public static String formatCpf(String cpf) {
        return cpf.replaceAll("\\.|-","");
    }

    public static boolean checkCpf(String cpf) {
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

    public static String validarCnpj(String cnpj) {
        if (!checkCnpj(cnpj)) {
            LOG.error("O CNPJ é inválido!");
            throw new RuntimeException("CNPJ inválido");
        }
        return regexCnpj(cnpj);
    }

    public static String regexCnpj(String cnpj) {
        return cnpj.replaceAll("\\.|-|/","");
    }

    public static boolean checkCnpj(String cnpj) {
        cnpj = regexCnpj(cnpj);
        System.out.println(cnpj);
        int digv1 = Character.getNumericValue(cnpj.charAt(12));
        int digv2 = Character.getNumericValue(cnpj.charAt(13));
        int mult1, mult2, soma = 0, resto1 = 0, resto2 = 0;

        //verificar o primeiro dígito
        mult1 = 5;
        mult2 = 9;

        for (int i = 0; i < 4; ++i) {
            int digito = Character.getNumericValue(cnpj.charAt(i));
            soma += digito * mult1;
            --mult1;
        }
        for (int i = 0; i < 8; ++i) {
            int digito = Character.getNumericValue(cnpj.charAt(i+4));
            soma += digito * mult2;
            --mult2;
        }

        if (((soma) % 11) < 2) {
            resto1 = 0;
        } else {
            resto1 = 11 - ((soma) % 11);
        }

        //verificar o segundo dígito
        soma = 0;
        mult1 = 6;
        mult2 = 9;

        for (int i = 0; i < 5; ++i) {
            int digito = Character.getNumericValue(cnpj.charAt(i));
            soma += digito * mult1;
            --mult1;
        }
        for (int i = 0; i < 8; ++i) {
            int digito = Character.getNumericValue(cnpj.charAt(i+5));
            soma += digito * mult2;
            --mult2;
        }

        if (((soma) % 11) < 2) {
            resto2 = 0;
        } else {
            resto2 = 11 - ((soma) % 11);
        }

        return (digv1 == resto1 && digv2 == resto2);
    }

    public static void validarFilial(Empresa empresa) {
        if (empresa.getTipoEmpresa().equals(EnumTipoEmpresa.MATRIZ)) {
            empresa.setNomeFilial("");
        }
    }

    public static void validarEstado(String estado) {
        if (estado.isBlank()) {
            LOG.error("O nome do estado está nulo!");
            throw new RuntimeException("Estado nulo");
        } else if (estado.length() > 2) {
            LOG.error("O nome do estado é inválido!");
            throw new RuntimeException("Estado inválido");
        }
    }

    public static void validarEmail(String email) {
        if (!checkEmail(email)) {
            LOG.error("O e-mail do usuário é inválido!");
            throw new RuntimeException("E-mail inválido");
        }
    }

    public static boolean checkEmail(String email) {
        String rePattern = "(.+)@(.+)\\.(.+)";
        return email.matches(rePattern);
    }

    public static void validarSenha(String senha) {
        if (!checkSenha(senha)) {
            LOG.error("A senha do usuário é inválida!");
            throw new RuntimeException("Senha inválida");
        }
    }

    public static boolean checkSenha(String senha) {
        return senha.length() >= 8 && senha.matches(".*\\d.*") && senha.matches(".*\\w.*");
    }
}
