package habilitpro.model.persistence.usuario;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "perfis")
    private ArrayList<Usuario> usuarios;

    public Perfil() {
    }

    public Perfil(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getnome() {
        return nome;
    }

    public void setnome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
