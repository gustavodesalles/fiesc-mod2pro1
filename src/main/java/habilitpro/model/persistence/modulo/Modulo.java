package habilitpro.model.persistence.modulo;

import habilitpro.enums.EnumStatusModulo;
import habilitpro.model.persistence.trilha.Trilha;
import javax.persistence.*;

import java.time.OffsetDateTime;

@Entity
public class Modulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "trilha_id")
    private Trilha trilha;

    @Column(nullable = false)
    private String nome;

    private String habilidades;
    private String tarefa;
    private OffsetDateTime dataInicio;
    private OffsetDateTime dataFim;
    private OffsetDateTime dataPrazo;

    @Column(nullable = false)
    private int prazoLimite;

    @Column(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private EnumStatusModulo status;

    public Modulo() {
    }

    public Modulo(Trilha trilha, String nome, String habilidades, String tarefa) {
        this.trilha = trilha;
        this.nome = nome.toUpperCase();
        this.habilidades = habilidades.toUpperCase();
        this.tarefa = tarefa.toUpperCase();
        this.dataInicio = null;
        this.dataFim = null;
        this.dataPrazo = null;
        this.prazoLimite = 10;
        this.status = EnumStatusModulo.NAO_INIC;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public String getTarefa() {
        return tarefa;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    public OffsetDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(OffsetDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public OffsetDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(OffsetDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public OffsetDateTime getDataPrazo() {
        return dataPrazo;
    }

    public void setDataPrazo(OffsetDateTime dataPrazo) {
        this.dataPrazo = dataPrazo;
    }

    public int getPrazoLimite() {
        return prazoLimite;
    }

    public void setPrazoLimite(int prazoLimite) {
        this.prazoLimite = prazoLimite;
    }

    public EnumStatusModulo getStatus() {
        return status;
    }

    public void setStatus(EnumStatusModulo status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Modulo{");
        sb.append("id=").append(id);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", habilidades='").append(habilidades).append('\'');
        sb.append(", tarefa='").append(tarefa).append('\'');
        sb.append(", dataInicio=").append(dataInicio);
        sb.append(", dataFim=").append(dataFim);
        sb.append(", dataPrazo=").append(dataPrazo);
        sb.append(", prazoLimite=").append(prazoLimite);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
