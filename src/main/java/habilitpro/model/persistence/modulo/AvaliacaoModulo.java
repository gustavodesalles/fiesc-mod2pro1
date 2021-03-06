package habilitpro.model.persistence.modulo;

import habilitpro.model.persistence.trabalhador.Trabalhador;
import javax.persistence.*;

@Entity
public class AvaliacaoModulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public int notaAvaliacao;
    public String anotacoes;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    public Modulo modulo;

    @ManyToOne
    @JoinColumn(name = "trabalhador_id")
    public Trabalhador trabalhador;

    public AvaliacaoModulo() {
    }

    public AvaliacaoModulo(Modulo modulo, Trabalhador trabalhador) {
        this.notaAvaliacao = 0;
        this.anotacoes = null;
        this.modulo = modulo;
        this.trabalhador = trabalhador;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNotaAvaliacao() {
        return notaAvaliacao;
    }

    public void setNotaAvaliacao(int notaAvaliacao) {
        if (notaAvaliacao >= 1 && notaAvaliacao <= 5) this.notaAvaliacao = notaAvaliacao;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Trabalhador getTrabalhador() {
        return trabalhador;
    }

    public void setTrabalhador(Trabalhador trabalhador) {
        this.trabalhador = trabalhador;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AvaliacaoModulo{");
        sb.append("id=").append(id);
        sb.append(", notaAvaliacao=").append(notaAvaliacao);
        sb.append(", anotacoes='").append(anotacoes).append('\'');
        sb.append(", modulo=").append(modulo);
        sb.append('}');
        return sb.toString();
    }
}
