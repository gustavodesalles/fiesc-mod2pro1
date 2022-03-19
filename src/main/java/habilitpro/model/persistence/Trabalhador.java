package habilitpro.model.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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
    private ArrayList<Trilha> trilhas;
    private LinkedHashMap<Modulo, AvaliacaoModulo> modulosComAv;

    public Trabalhador() {
    }

    public Trabalhador(String nome, String cpf, Empresa empresa, String setor, String funcao, LocalDate dataUltimaAlter, ArrayList<Trilha> trilhas, LinkedHashMap<Modulo, AvaliacaoModulo> modulosComAv) {
        this.nome = nome;
        this.cpf = cpf;
        this.empresa = empresa;
        this.setor = setor;
        this.funcao = funcao;
        this.dataUltimaAlter = dataUltimaAlter;
        this.trilhas = trilhas;
        this.modulosComAv = modulosComAv;
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

    public ArrayList<Trilha> getTrilhas() {
        return trilhas;
    }

    public void setTrilhas(ArrayList<Trilha> trilhas) {
        this.trilhas = trilhas;
    }

    public LinkedHashMap<Modulo, AvaliacaoModulo> getModulosComAv() {
        return modulosComAv;
    }

    public void setModulosComAv(LinkedHashMap<Modulo, AvaliacaoModulo> modulosComAv) {
        this.modulosComAv = modulosComAv;
    }
}
