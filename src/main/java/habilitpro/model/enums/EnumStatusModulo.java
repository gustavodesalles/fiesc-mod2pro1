package habilitpro.model.enums;

public enum EnumStatusModulo {
    NAO_INIC("Curso não iniciado"),
    ANDAMENTO("Curso em andamento"),
    FASE("Em fase de avaliação"),
    AV_FINALIZADA("Fase de avaliação finalizada");

    private String descricao;

    EnumStatusModulo(String descricao) { this.descricao = descricao; }
}
