package habilitpro.services.trabalhador;

import habilitpro.model.dao.modulo.AvaliacaoModuloDAO;
import habilitpro.model.dao.trabalhador.TrabalhadorDAO;
import habilitpro.model.persistence.empresa.Empresa;
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
        if (!funcao.getSetor().getEmpresa().equals(empresa)) {
            LOG.error("A função não pertence à empresa!");
            throw new RuntimeException("Função não corresponde");
        }

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
        Validar.validarId(id);

        LOG.info("Preparando para encontrar o trabalhador.");
        Trabalhador trabalhador = getById(id);

        if (trabalhadorDAO.checkIfAvaliacaoModulo(trabalhador)) {
            LOG.error("O trabalhador ainda possui avaliações; delete-as antes de deletar o trabalhador.");
            throw new RuntimeException("Trabalhador possui avaliações");
        }

        for (Trilha t : trabalhador.getTrilhas()) {
            trabalhador.getTrilhas().remove(trabalhador);
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

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
