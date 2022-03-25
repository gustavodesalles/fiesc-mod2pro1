package habilitpro.services.empresa;

import habilitpro.model.dao.empresa.EmpresaDAO;
import habilitpro.model.enums.EnumTipoEmpresa;
import habilitpro.model.persistence.empresa.Empresa;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import habilitpro.model.persistence.empresa.Setor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EmpresaService {
    private static final Logger LOG = LogManager.getLogger(EmpresaService.class);
    private EntityManager entityManager;
    private EmpresaDAO empresaDAO;
    private SetorService setorService;

    public EmpresaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.empresaDAO = new EmpresaDAO(entityManager);
        this.setorService = new SetorService(entityManager);
    }

    public void create(Empresa empresa) {
        LOG.info("Preparando para criar uma empresa.");

        validateIfNull(empresa);

        validarNome(empresa.getNome());
        
        empresa.setCnpj(validarCnpj(empresa.getCnpj()));

        validarFilial(empresa);

        String cidade = empresa.getCidade();
        validarCidade(cidade);

        String estado = empresa.getEstado();
        validarEstado(estado);

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

    private void validarFilial(Empresa empresa) {
        if (empresa.getTipoEmpresa().equals(EnumTipoEmpresa.MATRIZ)) {
            empresa.setNomeFilial("");
        }
    }

    private void validarEstado(String estado) {
        if (estado.isBlank() || estado == null) {
            LOG.error("O nome do estado está nulo!");
            throw new RuntimeException("Estado nulo");
        } else if (estado.length() > 2) {
            LOG.error("O nome do estado é inválido!");
            throw new RuntimeException("Estado inválido");
        }
    }

    private void validarCidade(String cidade) {
        if (cidade.isBlank() || cidade == null) {
            LOG.error("O nome da cidade está nulo!");
            throw new RuntimeException("Cidade nula");
        }
    }

    private String validarCnpj(String cnpj) {
        if (!checkCnpj(cnpj)) {
            LOG.error("O CNPJ é inválido!");
            throw new RuntimeException("CNPJ inválido");
        }
        return regexCnpj(cnpj);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            LOG.error("O nome da empresa está nulo!");
            throw new RuntimeException("Nome nulo");
        }
    }

    public void delete(Long id) {
        if (id == null) {
            LOG.error("O ID da empresa está nulo!");
            throw new RuntimeException("ID nulo");
        }

        LOG.info("Preparando para encontrar a empresa.");

        Empresa empresa = empresaDAO.getById(id);
        validateIfNull(empresa);
        LOG.info("Empresa encontrada!");

        beginTransaction();
        try {
            empresaDAO.delete(empresa);
        } catch (Exception e) {
            LOG.error("Erro ao deletar a empresa, causado por: " + e.getMessage());
            throw new RuntimeException(e);
        }
        commitAndCloseTransaction();
        LOG.info("Empresa deletada com sucesso!");
    }

    public void update(Empresa novaEmpresa, Long id) {
        if (novaEmpresa == null || id == null) {
            LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parâmetro nulo");
        }

        LOG.info("Preparando para encontrar a empresa.");
        Empresa empresa = empresaDAO.getById(id);
        validateIfNull(empresa);
        LOG.info("Empresa encontrada!");

        validateIfNull(novaEmpresa);

        validarNome(novaEmpresa.getNome());

        novaEmpresa.setCnpj(validarCnpj(novaEmpresa.getCnpj()));

        validarFilial(novaEmpresa);

        String cidade = novaEmpresa.getCidade();
        validarCidade(cidade);

        String estado = novaEmpresa.getEstado();
        validarEstado(estado);

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

    public String regexCnpj(String cnpj) {
        return cnpj.replaceAll("\\.|-|/","");
    }

    public boolean checkCnpj(String cnpj) {
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
