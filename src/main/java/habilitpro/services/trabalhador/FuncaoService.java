package habilitpro.services.trabalhador;

import habilitpro.model.dao.trabalhador.FuncaoDAO;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Funcao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

public class FuncaoService {
    private static final Logger LOG = LogManager.getLogger(FuncaoService.class);
    private EntityManager entityManager;
    private FuncaoDAO funcaoDAO;

    public FuncaoService(EntityManager entityManager, FuncaoDAO funcaoDAO) {
        this.entityManager = entityManager;
        this.funcaoDAO = funcaoDAO;
    }

    public void create(Funcao funcao) {
        validateIfNull(funcao);

        LOG.info("Preparando para criar a função.");

        String funcaoNome = funcao.getNome();
        if (funcaoNome == null || funcaoNome.isBlank()) {
            LOG.error("O nome da função está nulo!");
            throw new RuntimeException("Nome nulo");
        }

        Setor funcaoSetor = funcao.getSetor();
        if (funcaoSetor == null) {
            LOG.error("O setor da função está nulo!");
            throw new EntityNotFoundException("Setor nulo");
        }

        beginTransaction();
        funcaoDAO.create(funcao);
        commitAndCloseTransaction();
        LOG.info("Função criada com sucesso!");
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID da função está nulo!");
            throw new RuntimeException("ID nulo");
        }

        LOG.info("Preparando para encontrar a função.");

        Funcao funcao = funcaoDAO.getById(id);
        validateIfNull(funcao);
        LOG.info("Função encontrada!");

        beginTransaction();
        funcaoDAO.delete(funcao);
        commitAndCloseTransaction();
        LOG.info("Função deletada com sucesso!");
    }

    public void update(Funcao novaFuncao, Long id) {
        if (novaFuncao == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a função.");
        Funcao funcao = funcaoDAO.getById(id);
        validateIfNull(funcao);
        LOG.info("Função encontrada!");

        beginTransaction();
        funcao.setNome(novaFuncao.getNome());
        funcao.setSetor(novaFuncao.getSetor());
        commitAndCloseTransaction();
        LOG.info("Função atualizada com sucesso!");
    }

    public Funcao findByName(String name) {
        if (name == null || name.isEmpty()) {
            LOG.error("O nome da função está nulo!");
            throw new RuntimeException("Nome nulo");
        }

        try {
            Funcao funcao = this.funcaoDAO.findByName(name.toLowerCase());
            return funcao;
        } catch (NoResultException n) {
            this.LOG.error("Função não encontrada.");
            return null;
        }
    }

    public List<Funcao> listAll() {
        LOG.info("Preparando para listar as funções.");
        List<Funcao> funcoes = funcaoDAO.listAll();

        if (funcoes == null) {
            LOG.error("Não foram encontradas funções!");
            throw new RuntimeException("Não há funções");
        }

        LOG.info("Número de funções encontradas: " + funcoes.size());
        return funcoes;
    }

    public List<Funcao> listBySetor(Setor setor) {
        LOG.info("Preparando para listar as funções do setor: " + setor.getNome());
        List<Funcao> funcoes = funcaoDAO.listBySetor(setor);

        if (funcoes == null) {
            LOG.error("Não foram encontradas funções no setor " + setor.getNome() + "!");
            throw new RuntimeException("Não há funções no setor informado");
        }

        LOG.info("Número de funções encontradas: " + funcoes.size());
        return funcoes;
    }

    private void validateIfNull(Funcao funcao) {
        if (funcao == null) {
            this.LOG.error("A função não existe!");
            throw new EntityNotFoundException("Função nula");
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
