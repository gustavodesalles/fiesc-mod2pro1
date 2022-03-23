package habilitpro.model.persistence.trilha;

import jakarta.persistence.*;

@Entity
public class Ocupacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "trilha_id")
    private Trilha trilha;

    @Column(nullable = false)
    private String nome;

    public Ocupacao() {
    }

    public Ocupacao(Trilha trilha, String nome) {
        this.trilha = trilha;
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Trilha getTrilha() {
        return trilha;
    }

    public void setTrilha(Trilha trilha) {
        this.trilha = trilha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Ocupacao{");
        sb.append("id=").append(id);
        sb.append(", trilha=").append(trilha);
        sb.append(", nome='").append(nome).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
