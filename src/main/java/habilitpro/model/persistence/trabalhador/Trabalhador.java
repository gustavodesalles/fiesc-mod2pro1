package habilitpro.model.persistence.trabalhador;

import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.modulo.AvaliacaoModulo;
import habilitpro.model.persistence.trilha.Trilha;
import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Trabalhador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;

    private LocalDate dataUltimaAlter;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "trabalhadores")
    private List<Trilha> trilhas;

    @OneToMany(mappedBy = "trabalhador")
    private List<AvaliacaoModulo> modulosComAv;

    public Trabalhador() {
    }

    public Trabalhador(String nome, String cpf, Empresa empresa, Funcao funcao) {
        this.nome = nome.toUpperCase();
        this.cpf = cpf;
        this.empresa = empresa;
        this.funcao = funcao;
        this.dataUltimaAlter = null;
        this.trilhas = new ArrayList<Trilha>();
        this.modulosComAv = new ArrayList<AvaliacaoModulo>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public LocalDate getDataUltimaAlter() {
        return dataUltimaAlter;
    }

    public void setDataUltimaAlter(LocalDate dataUltimaAlter) {
        this.dataUltimaAlter = dataUltimaAlter;
    }

    public List<Trilha> getTrilhas() {
        return trilhas;
    }

    public void setTrilhas(List<Trilha> trilhas) {
        this.trilhas = trilhas;
    }

    public List<AvaliacaoModulo> getModulosComAv() {
        return modulosComAv;
    }

    public void setModulosComAv(List<AvaliacaoModulo> modulosComAv) {
        this.modulosComAv = modulosComAv;
    }
}
