package habilitpro.services;

import habilitpro.model.dao.EmpresaDAO;
import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.Empresa;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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

        validateIfNull(empresa);

        String nome = empresa.getNome();
        if (nome.isEmpty()) {
            LOG.error("O nome da empresa está nulo!");
            throw new RuntimeException("Nome nulo");
        }

        String cnpj = empresa.getCnpj();
        if (!checkCnpj(empresa.getCnpj())) {
            LOG.error("O CNPJ é inválido!");
            throw new RuntimeException("CNPJ inválido");
        }

        if (empresa.getTipoEmpresa().equals(EnumTipoEmpresa.MATRIZ)) {
            empresa.setNomeFilial("");
        }

        String cidade = empresa.getCidade();
        if (cidade.isEmpty()) {
            LOG.error("O nome da cidade está nulo!");
            throw new RuntimeException("Cidade nula");
        }

        String estado = empresa.getEstado();
        if (estado.isEmpty()) {
            LOG.error("O nome do estado está nulo!");
            throw new RuntimeException("Estado nulo");
        }

        try {
            empresa.setNome(empresa.getNome().toLowerCase());
            empresa.setCidade(cidade.toLowerCase());
            empresa.setEstado(estado.toLowerCase());

            beginTransaction();
            empresaDAO.create(empresa);
            commitAndCloseTransaction();
        } catch (Exception e) {
            LOG.error("Erro ao criar a empresa, causado por :" + e.getMessage());
        }
        LOG.info("Empresa criada com sucesso!");
    }

    public void delete(Long id) {
        LOG.info("Preparando para encontrar a empresa.");

        if (id == null) {
            LOG.error("O ID da empresa está nulo!");
            throw new RuntimeException("ID nulo");
        }

        Empresa empresa = empresaDAO.getById(id);
        validateIfNull(empresa);

        beginTransaction();
        empresaDAO.delete(empresa);
        commitAndCloseTransaction();
        LOG.info("Empresa deletada com sucesso!");
    }

    public void update(Empresa novaEmpresa, Long id) {
        if (novaEmpresa == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        Empresa empresa = empresaDAO.getById(id);
        validateIfNull(empresa);
        LOG.info("Empresa encontrada!");

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

    public String regexCnpj(String cnpj) {
        String rePattern = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}";
        return cnpj.replaceAll(rePattern, "");
    }

    public boolean checkCnpj(String cnpj) {
        cnpj = regexCnpj(cnpj);
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

    private void validateIfNull(Empresa empresa) {
        if (empresa == null) {
            this.LOG.error("A empresa não existe!");
            throw new EntityNotFoundException("Empresa nula");
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
