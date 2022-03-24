package habilitpro.services.trabalhador;

import habilitpro.model.dao.trabalhador.TrabalhadorDAO;
import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import habilitpro.model.persistence.trilha.Trilha;
import habilitpro.services.trilha.TrilhaService;
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
    private TrilhaService trilhaService;

    public TrabalhadorService(EntityManager entityManager, TrabalhadorDAO trabalhadorDAO, TrilhaService trilhaService) {
        this.entityManager = entityManager;
        this.trabalhadorDAO = trabalhadorDAO;
        this.trilhaService = trilhaService;
    }

    public void create(Trabalhador trabalhador) {
        validateIfNull(trabalhador);

        LOG.info("Preparando para criar o trabalhador.");

        String nome = trabalhador.getNome();
        if (nome == null || nome.isBlank()) {
            LOG.error("O nome do trabalhador está nulo!");
            throw new RuntimeException("Nome nulo");
        }

        String cpf = trabalhador.getCpf();
        if (!checkCpf(cpf)) {
            LOG.error("O CPF do trabalhador é inválido!");
            throw new RuntimeException("CPF inválido");
        }

        Empresa empresa = trabalhador.getEmpresa();
        if (empresa == null) {
            LOG.error("A empresa está nula!");
            throw new EntityNotFoundException("Empresa nula");
        }

        Setor setor = trabalhador.getSetor();
        if (setor == null) {
            LOG.error("O setor está nulo!");
            throw new EntityNotFoundException("Setor nulo");
        }

        Funcao funcao = trabalhador.getFuncao();
        if (funcao == null) {
            LOG.error("A função está nula!");
            throw new EntityNotFoundException("Função nula");
        }

        beginTransaction();
        trabalhadorDAO.create(trabalhador);
        commitAndCloseTransaction();
        LOG.info("Trabalhador criado com sucesso!");
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID do trabalhador está nulo!");
            throw new EntityNotFoundException("ID nulo");
        }

        LOG.info("Preparando para encontrar o trabalhador.");
        Trabalhador trabalhador = getById(id);
        validateIfNull(trabalhador);

        beginTransaction();
        trabalhadorDAO.delete(trabalhador);
        commitAndCloseTransaction();
        LOG.info("Trabalhador deletado com sucesso!");
    }

    public void update(Trabalhador novoTrab, Long id) {
        if (novoTrab == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new EntityNotFoundException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar o trabalhador.");
        Trabalhador trabalhador = getById(id);
        validateIfNull(trabalhador);

        beginTransaction();
        trabalhador.setNome(novoTrab.getNome());
        trabalhador.setCpf(novoTrab.getCpf());
        trabalhador.setEmpresa(novoTrab.getEmpresa());
        trabalhador.setSetor(novoTrab.getSetor());
        trabalhador.setFuncao(novoTrab.getFuncao());
        trabalhador.setDataUltimaAlter(LocalDate.now());
        trabalhador.setTrilhas(novoTrab.getTrilhas());
        trabalhador.setModulosComAv(novoTrab.getModulosComAv());
        commitAndCloseTransaction();
        LOG.info("Trabalhador atualizado com sucesso!");
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

    public List<Trabalhador> listByTrilha(Trilha trilha) {
        if (trilha == null) {
            LOG.error("A trilha está nula!");
            throw new EntityNotFoundException("Trilha nula");
        }

        LOG.info("Preparando para listar os trabalhadores na trilha: " + trilha.getNome());
        List<Trabalhador> trabalhadores = trabalhadorDAO.listByTrilha(trilha);

        if (trabalhadores == null) {
            LOG.info("Não foram encontrados trabalhadores na trilha " + trilha.getNome() + "!");
            throw new EntityNotFoundException("Não há trabalhadores na trilha informada");
        }

        LOG.info("Número de trabalhadores encontrados: " + trabalhadores.size());
        return trabalhadores;
    }

    public Trabalhador getById(Long id) {
        if (id == null) {
            LOG.error("O ID do trabalhador está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Trabalhador trabalhador = trabalhadorDAO.getById(id);

        if (trabalhador == null) {
            LOG.error("Trabalhador não encontrado!");
            throw new EntityNotFoundException("Trabalhador nulo");
        }

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
