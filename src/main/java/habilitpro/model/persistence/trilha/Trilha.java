package habilitpro.model.persistence.trilha;

import habilitpro.model.persistence.empresa.Empresa;
import habilitpro.model.persistence.trabalhador.Trabalhador;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Trilha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "ocupacao_id")
    private Ocupacao ocupacao;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String apelido;

    private int nivelSatisfacao;
    private String anotacoes;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Trabalhador> trabalhadores;

    public Trilha() {
    }

    public Trilha(Empresa empresa, Ocupacao ocupacao, int nivelSatisfacao, String anotacoes) {
        this.empresa = empresa;
        this.ocupacao = ocupacao;
        this.nome = "";
        this.apelido = "";
        this.nivelSatisfacao = nivelSatisfacao;
        this.anotacoes = anotacoes;
        this.trabalhadores = new ArrayList<Trabalhador>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Ocupacao getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(Ocupacao ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public int getNivelSatisfacao() {
        return nivelSatisfacao;
    }

    public void setNivelSatisfacao(int nivelSatisfacao) {
        this.nivelSatisfacao = nivelSatisfacao;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public List<Trabalhador> getTrabalhadores() {
        return trabalhadores;
    }

    public void setTrabalhadores(List<Trabalhador> trabalhadores) {
        this.trabalhadores = trabalhadores;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trilha{");
        sb.append("id=").append(id);
        sb.append(", empresa=").append(empresa);
        sb.append(", ocupacao='").append(ocupacao).append('\'');
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", apelido='").append(apelido).append('\'');
        sb.append(", nivelSatisfacao=").append(nivelSatisfacao);
        sb.append(", anotacoes='").append(anotacoes).append('\'');
        sb.append(", trabalhadores=").append(trabalhadores);
        sb.append('}');
        return sb.toString();
    }
}
