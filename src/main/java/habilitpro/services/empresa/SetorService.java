package habilitpro.services.empresa;

import habilitpro.model.dao.empresa.SetorDAO;
import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.empresa.Setor;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

public class SetorService {
    private static final Logger LOG = LogManager.getLogger(SetorService.class);
    private EntityManager entityManager;
    private SetorDAO setorDAO;

    public SetorService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.setorDAO = new SetorDAO(entityManager);
    }

    public void create(Setor setor) {
        Validar.validarSetor(setor);

        LOG.info("Preparando para criar o setor.");

        Empresa empresa = setor.getEmpresa();
        Validar.validarEmpresa(empresa);

        String nome = setor.getNome();
        Validar.validarString(nome);

        LOG.info("Buscando setor " + nome);
        Setor setor1 = findByName(nome);
        if (setor1 != null) {
            LOG.error("Este setor já existe!");
            throw new RuntimeException("Setor já existe");
        }

        beginTransaction();
        try {
            setorDAO.create(setor);
        } catch (Exception e) {
            LOG.error("Erro ao criar o setor, causado por: " + e.getMessage());
        }
        commitAndCloseTransaction();
        LOG.info("Setor criado com sucesso!");
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID do setor está nulo!");
            throw new RuntimeException("ID nulo");
        }

        LOG.info("Preparando para encontrar o setor.");

        Setor setor = getById(id);

        if (setorDAO.checkIfFuncao(setor)) {
            LOG.error("O setor ainda possui funções; delete-as antes de deletar o setor.");
            throw new RuntimeException("Setor possui funções");
        }

        try {
            beginTransaction();
            setorDAO.delete(setor);
            commitAndCloseTransaction();
            LOG.info("Setor deletado com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar o setor, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Setor novoSetor, Long id) {
        if (novoSetor == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar o setor.");
        Setor setor = getById(id);
        Validar.validarSetor(setor);
        LOG.info("Setor encontrado!");

        beginTransaction();
        setor.setEmpresa(novoSetor.getEmpresa());
        setor.setNome(novoSetor.getNome());
        commitAndCloseTransaction();
        LOG.info("Setor atualizado com sucesso!");
    }

    public Setor getById(Long id) {
        if (id == null) {
            LOG.error("O ID do setor está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Setor setor = setorDAO.getById(id);

        Validar.validarSetor(setor);

        LOG.info("Setor encontrado!");
        return setor;
    }

    public Setor findByName(String name) {
        if (name == null || name.isEmpty()) {
            LOG.error("O nome do setor está nulo!");
            throw new RuntimeException("Nome nulo");
        }

        try {
            Setor setor = setorDAO.findByName(name);
            return setor;
        } catch (NoResultException n) {
            this.LOG.error("Setor não encontrado.");
            return null;
        }
    }

    public List<Setor> listAll() {
        LOG.info("Preparando para listar os setores.");
        List<Setor> setores = setorDAO.listAll();

        if (setores == null) {
            LOG.error("Não foram encontrados setores!");
            throw new RuntimeException("Não há setores");
        }

        LOG.info("Número de setores encontrados: " + setores.size());
        return setores;
    }

    public List<Setor> listByEmpresa(Empresa empresa) {
        Validar.validarEmpresa(empresa);

        LOG.info("Preparando para listar os setores da empresa: " + empresa.getNome());
        List<Setor> setores = setorDAO.listByEmpresa(empresa);

        if (setores == null) {
            LOG.error("Não foram encontrados setores da empresa " + empresa.getNome() + "!");
            throw new RuntimeException("Não há setores da empresa informada");
        }

        LOG.info("Número de setores encontrados: " + setores.size());
        return setores;
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
