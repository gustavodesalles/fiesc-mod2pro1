package habilitpro.services.empresa;

import habilitpro.model.dao.empresa.EmpresaDAO;
import habilitpro.model.dao.empresa.SetorDAO;
import habilitpro.model.dao.trabalhador.FuncaoDAO;
import habilitpro.model.dao.trilha.TrilhaDAO;
import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import habilitpro.model.persistence.empresa.Setor;
import habilitpro.model.persistence.trabalhador.Funcao;
import habilitpro.utils.Validar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EmpresaService {
    private static final Logger LOG = LogManager.getLogger(EmpresaService.class);
    private EntityManager entityManager;
    private EmpresaDAO empresaDAO;

    public EmpresaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.empresaDAO = new EmpresaDAO(entityManager);
    }

    public void create(Empresa empresa) {
        LOG.info("Preparando para criar uma empresa.");

        Validar.validarEmpresa(empresa);

        Validar.validarString(empresa.getNome());

        empresa.setCnpj(Validar.validarCnpj(empresa.getCnpj()));

        Validar.validarFilial(empresa);

        String cidade = empresa.getCidade();
        Validar.validarString(cidade);

        String estado = empresa.getEstado();
        Validar.validarEstado(estado);

        try {
            empresa.setNome(empresa.getNome().toUpperCase());
            empresa.setCidade(cidade.toUpperCase());
            empresa.setEstado(estado.toUpperCase());

            beginTransaction();
            empresaDAO.create(empresa);
            commitAndCloseTransaction();
        } catch (Exception e) {
            LOG.error("Erro ao criar a empresa, causado por :" + e.getMessage());
        }
        LOG.info("Empresa criada com sucesso!");
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID da empresa está nulo!");
            throw new RuntimeException("ID nulo");
        }

        LOG.info("Preparando para encontrar a empresa.");

        Empresa empresa = empresaDAO.getById(id);
        Validar.validarEmpresa(empresa);
        LOG.info("Empresa encontrada!");

        if (empresaDAO.checkIfSetor(empresa)) {
            LOG.error("A empresa ainda possui setores; por favor, delete-os antes de deletar a empresa.");
            throw new RuntimeException("Empresa possui setores");
        }

        if (empresaDAO.checkIfTrabalhador(empresa)) {
            LOG.error("A empresa ainda possui trabalhadores; por favor, delete-os antes de deletar a empresa.");
            throw new RuntimeException("Empresa possui trabalhadores");
        }

        if (empresaDAO.checkIfTrilha(empresa)) {
            LOG.error("A empresa ainda possui trilhas; por favor, delete-as antes de deletar a empresa.");
            throw new RuntimeException("Empresa possui trilhas");
        }

        try {
            beginTransaction();
            empresaDAO.delete(empresa);
            commitAndCloseTransaction();
            LOG.info("Empresa deletada com sucesso!");
        } catch (Exception e) {
            LOG.error("Erro ao deletar a empresa, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(Empresa novaEmpresa, Long id) {
        if (novaEmpresa == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a empresa.");
        Empresa empresa = empresaDAO.getById(id);
        Validar.validarEmpresa(empresa);
        LOG.info("Empresa encontrada!");

        Validar.validarEmpresa(novaEmpresa);

        Validar.validarString(novaEmpresa.getNome());

        novaEmpresa.setCnpj(Validar.validarCnpj(novaEmpresa.getCnpj()));

        Validar.validarFilial(novaEmpresa);

        String cidade = novaEmpresa.getCidade();
        Validar.validarString(cidade);

        String estado = novaEmpresa.getEstado();
        Validar.validarEstado(estado);

        beginTransaction();
        empresa.setNome(novaEmpresa.getNome());
        empresa.setCnpj(novaEmpresa.getCnpj());
        empresa.setTipoEmpresa(novaEmpresa.getTipoEmpresa());
        empresa.setNomeFilial(novaEmpresa.getNomeFilial());
        empresa.setSegmento(novaEmpresa.getSegmento());
        empresa.setCidade(novaEmpresa.getCidade());
        empresa.setEstado(novaEmpresa.getEstado());
        empresa.setRegionalSenai(novaEmpresa.getRegionalSenai());
        commitAndCloseTransaction();
        LOG.info("Empresa atualizada com sucesso!");
    }

    public Empresa getById(Long id) {
        if (id == null) {
            LOG.error("O ID da empresa está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Empresa empresa = empresaDAO.getById(id);

        if (empresa == null) {
            LOG.error("Empresa não encontrada!");
            throw new RuntimeException("Empresa nula");
        }

        LOG.info("Empresa encontrada!");
        return empresa;
    }

    public List<Empresa> listAll() {
        LOG.info("Preparando para listar as empresas.");
        List<Empresa> empresas = empresaDAO.listAll();

        if (empresas == null) {
            LOG.error("Não foram encontradas empresas!");
            throw new RuntimeException("Não há empresas");
        }

        LOG.info("Número de empresas encontradas: " + empresas.size());
        return empresas;
    }

    public List<Empresa> listByTipo(EnumTipoEmpresa tipoEmpresa) {
        if (tipoEmpresa == null) {
            LOG.error("O tipo de empresa está nulo!");
            throw new RuntimeException("Tipo nulo");
        }

        LOG.info("Preparando para buscar as empresas do tipo: " + tipoEmpresa);
        List<Empresa> empresas = empresaDAO.listByTipo(tipoEmpresa);

        if (empresas == null) {
            LOG.error("Não foram encontradas empresas do tipo " + tipoEmpresa + "!");
            throw new RuntimeException("Não há empresas do tipo");
        }

        LOG.info("Número de empresas encontradas: " + empresas.size());
        return empresas;
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
