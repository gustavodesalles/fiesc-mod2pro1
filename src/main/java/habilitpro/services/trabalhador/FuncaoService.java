package habilitpro.services.trabalhador;

import habilitpro.model.dao.trabalhador.FuncaoDAO;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class FuncaoService {
    private static final Logger LOG = LogManager.getLogger(FuncaoService.class);
    private EntityManager entityManager;
    private FuncaoDAO funcaoDAO;

    public FuncaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.funcaoDAO = new FuncaoDAO(entityManager);
    }

    public void create(Funcao funcao) {
        Validar.validarFuncao(funcao);

        LOG.info("Preparando para criar a função.");

        String nome = funcao.getNome();
        Validar.validarString(nome);

        Setor setor = funcao.getSetor();
        Validar.validarSetor(setor);

        LOG.info("Buscando função " + nome);
        Funcao funcao1 = findByName(nome);
        if (funcao1 != null && funcao1.getSetor().getNome().equals(funcao.getSetor().getNome())) {
            LOG.error("Esta função já existe!");
            throw new RuntimeException("Função já existe");
        }

        try {
            beginTransaction();
            funcaoDAO.create(funcao);
            commitAndCloseTransaction();
            LOG.info("Função criada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao criar a função, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        Validar.validarId(id);

        LOG.info("Preparando para encontrar a função.");

        Funcao funcao = getById(id);

        if (funcaoDAO.checkIfTrabalhador(funcao)) {
            LOG.error("A função ainda possui trabalhadores; delete-os antes de deletar a função.");
            throw new RuntimeException("Função possui trabalhadores");
        }

        try {
            beginTransaction();
            funcaoDAO.delete(funcao);
            commitAndCloseTransaction();
            LOG.info("Função deletada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar a função, causado por: " + e.getMessage());
        }
    }

    public void update(Funcao novaFuncao, Long id) {
        if (novaFuncao == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a função.");
        Funcao funcao = getById(id);

        try {
            beginTransaction();
            funcao.setNome(novaFuncao.getNome());
            funcao.setSetor(novaFuncao.getSetor());
            commitAndCloseTransaction();
            LOG.info("Função atualizada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao atualizar a função, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Funcao getById(Long id) {
        Validar.validarId(id);

        Funcao funcao = funcaoDAO.getById(id);

        Validar.validarFuncao(funcao);

        LOG.info("Função encontrada!");
        return funcao;
    }

    public Funcao findByName(String name) {
        Validar.validarString(name);

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

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
