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

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(nullable = false)
    private String setor;

    @Column(nullable = false)
    private String funcao;

    private LocalDate dataUltimaAlter;

    @ManyToMany(mappedBy = "trabalhadores")
    private List<Trilha> trilhas;

    @OneToMany(mappedBy = "trabalhador")
    private List<AvaliacaoModulo> modulosComAv;

    public Trabalhador() {
    }

    public Trabalhador(String nome, String cpf, Empresa empresa, String setor, String funcao, LocalDate dataUltimaAlter) {
        this.nome = nome;
        this.cpf = cpf;
        this.empresa = empresa;
        this.setor = setor;
        this.funcao = funcao;
        this.dataUltimaAlter = dataUltimaAlter;
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

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
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

    public void setTrilhas(ArrayList<Trilha> trilhas) {
        this.trilhas = trilhas;
    }

    public List<AvaliacaoModulo> getModulosComAv() {
        return modulosComAv;
    }

    public void setModulosComAv(ArrayList<AvaliacaoModulo> modulosComAv) {
        this.modulosComAv = modulosComAv;
    }
}
