package habilitpro.model.persistence.trabalhador;

import jakarta.persistence.*;

@Entity
public class Funcao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Setor setor;

    public Funcao() {
    }

    public Funcao(String name, Setor setor) {
        this.name = name;
        this.setor = setor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        sb.append(", name='").append(name).append('\'');
        sb.append(", setor=").append(setor);
        sb.append('}');
        return sb.toString();
    }
}
