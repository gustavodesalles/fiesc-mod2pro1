package habilitpro.model.persistence.trabalhador;

import habilitpro.model.persistence.empresa.Setor;

import javax.persistence.*;

@Entity
public class Funcao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private Setor setor;

    public Funcao() {
    }

    public Funcao(String nome, Setor setor) {
        this.nome = nome;
        this.setor = setor;
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

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Funcao{");
        sb.append("id=").append(id);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", setor=").append(setor);
        sb.append('}');
        return sb.toString();
    }
}
