package habilitpro.services.trabalhador;

import habilitpro.model.dao.modulo.AvaliacaoModuloDAO;
import habilitpro.model.dao.trabalhador.TrabalhadorDAO;
import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import habilitpro.model.persistence.trilha.Trilha;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class TrabalhadorService {
    private static final Logger LOG = LogManager.getLogger(TrabalhadorService.class);
    private EntityManager entityManager;
    private TrabalhadorDAO trabalhadorDAO;
    private AvaliacaoModuloDAO avaliacaoModuloDAO;

    public TrabalhadorService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.trabalhadorDAO = new TrabalhadorDAO(entityManager);
        this.avaliacaoModuloDAO = new AvaliacaoModuloDAO(entityManager);
    }

    public void create(Trabalhador trabalhador) {
        Validar.validarTrabalhador(trabalhador);

        LOG.info("Preparando para criar o trabalhador.");

        String nome = trabalhador.getNome();
        Validar.validarString(nome);

        String cpf = trabalhador.getCpf();
        Validar.validarCpf(cpf);

        Empresa empresa = trabalhador.getEmpresa();
        Validar.validarEmpresa(empresa);

        Funcao funcao = trabalhador.getFuncao();
        Validar.validarFuncao(funcao);

        try {
            beginTransaction();
            trabalhadorDAO.create(trabalhador);
            commitAndCloseTransaction();
            LOG.info("Trabalhador criado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao criar o trabalhador, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID do trabalhador está nulo!");
            throw new EntityNotFoundException("ID nulo");
        }

        LOG.info("Preparando para encontrar o trabalhador.");
        Trabalhador trabalhador = getById(id);

        if (trabalhadorDAO.checkIfAvaliacaoModulo(trabalhador)) {
            LOG.error("O trabalhador ainda possui avaliações; delete-as antes de deletar o trabalhador.");
            throw new RuntimeException("Trabalhador possui avaliações");
        }

        try {
            beginTransaction();
            trabalhadorDAO.delete(trabalhador);
            commitAndCloseTransaction();
            LOG.info("Trabalhador deletado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar o trabalhador, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Trabalhador novoTrab, Long id) {
        if (novoTrab == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new EntityNotFoundException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar o trabalhador.");
        Trabalhador trabalhador = getById(id);

        Validar.validarString(novoTrab.getNome());
        Validar.validarCpf(novoTrab.getCpf());
        Validar.validarEmpresa(novoTrab.getEmpresa());
        Validar.validarFuncao(novoTrab.getFuncao());

        try {
            beginTransaction();
            trabalhador.setNome(novoTrab.getNome());
            trabalhador.setCpf(novoTrab.getCpf());
            trabalhador.setEmpresa(novoTrab.getEmpresa());
            trabalhador.setFuncao(novoTrab.getFuncao());
            trabalhador.setDataUltimaAlter(LocalDate.now());
            trabalhador.setTrilhas(novoTrab.getTrilhas());
            trabalhador.setModulosComAv(novoTrab.getModulosComAv());
            commitAndCloseTransaction();
            LOG.info("Trabalhador atualizado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao atualizar o trabalhador, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Trabalhador> listAll() {
        LOG.info("Preparando para listar os trabalhadores.");
        List<Trabalhador> trabalhadores = trabalhadorDAO.listAll();

        if (trabalhadores == null) {
            LOG.info("Não foram encontrados trabalhadores!");
            throw new EntityNotFoundException("Não há trabalhadores");
        }

        LOG.info("Número de trabalhadores encontrados: " + trabalhadores.size());
        return trabalhadores;
    }

    public List<Trabalhador> listByFuncao(Funcao funcao) {
        Validar.validarFuncao(funcao);

        LOG.info("Preparando para listar os trabalhadores com a função: " + funcao.getNome());
        List<Trabalhador> trabalhadores = trabalhadorDAO.listByFuncao(funcao);

        if (trabalhadores == null) {
            LOG.info("Não foram encontrados trabalhadores com a função " + funcao.getNome() + "!");
            throw new EntityNotFoundException("Não há trabalhadores com a função informada");
        }

        LOG.info("Número de trabalhadores encontrados: " + trabalhadores.size());
        return trabalhadores;
    }

    public Trabalhador getById(Long id) {
        Validar.validarId(id);

        Trabalhador trabalhador = trabalhadorDAO.getById(id);

        Validar.validarTrabalhador(trabalhador);

        LOG.info("Trabalhador encontrado!");
        return trabalhador;
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

    private void validateIfNull(Trabalhador trabalhador) {
        if (trabalhador == null) {
            LOG.error("O trabalhador não existe!");
            throw new RuntimeException("Trabalhador nulo");
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
