package habilitpro.model.persistence;

import habilitpro.model.enums.EnumSegmentoEmpresa;
import habilitpro.model.enums.EnumTipoEmpresa;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private EnumTipoEmpresa tipoEmpresa;
    private String nomeFilial;

    @Column(nullable = false)
    private EnumSegmentoEmpresa segmento;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String regionalSenai;

    @OneToMany(mappedBy = "empresa")
    private ArrayList<Trilha> trilhas;

    public Empresa() {
    }

    public Empresa(long id, String nome, String cnpj, EnumTipoEmpresa tipoEmpresa, String nomeFilial, EnumSegmentoEmpresa segmento, String cidade, String estado, String regionalSenai, ArrayList<Trilha> trilhas) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.tipoEmpresa = tipoEmpresa;
        this.nomeFilial = nomeFilial;
        this.segmento = segmento;
        this.cidade = cidade;
        this.estado = estado;
        this.regionalSenai = regionalSenai;
        this.trilhas = trilhas;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public EnumTipoEmpresa getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(EnumTipoEmpresa tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getNomeFilial() {
        return nomeFilial;
    }

    public void setNomeFilial(String nomeFilial) {
        this.nomeFilial = nomeFilial;
    }

    public EnumSegmentoEmpresa getSegmento() {
        return segmento;
    }

    public void setSegmento(EnumSegmentoEmpresa segmento) {
        this.segmento = segmento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRegionalSenai() {
        return regionalSenai;
    }

    public void setRegionalSenai(String regionalSenai) {
        this.regionalSenai = regionalSenai;
    }

    public ArrayList<Trilha> getTrilhas() {
        return trilhas;
    }

    public void setTrilhas(ArrayList<Trilha> trilhas) {
        this.trilhas = trilhas;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Empresa{");
        sb.append("id=").append(id);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", cnpj='").append(cnpj).append('\'');
        sb.append(", tipoEmpresa=").append(tipoEmpresa);
        sb.append(", nomeFilial='").append(nomeFilial).append('\'');
        sb.append(", segmento=").append(segmento);
        sb.append(", cidade='").append(cidade).append('\'');
        sb.append(", estado='").append(estado).append('\'');
        sb.append(", regionalSenai='").append(regionalSenai).append('\'');
        sb.append(", trilhas=").append(trilhas);
        sb.append('}');
        return sb.toString();
    }
}
