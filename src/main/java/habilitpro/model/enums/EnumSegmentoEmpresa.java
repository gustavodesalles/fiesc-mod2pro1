package habilitpro.model.enums;

public enum EnumSegmentoEmpresa {
    ALIB("Alimentos e Bebidas"),
    CELP("Celulose e Papel"),
    CONS("Construção"),
    EELE("Equipamentos Elétricos"),
    FARM("Fármacos e Equipamentos de Saúde"),
    FUMO("Fumo"),
    IAUT("Indústria Automotiva"),
    ICER("Indústria Cerâmica"),
    IDIV("Indústria Diversa"),
    IEXT("Indústria Extrativa"),
    IGRF("Indústria Gráfica"),
    MADE("Madeira e Móveis"),
    MAQU("Máquinas e Equipamentos"),
    META("Metalmecânica e Metalurgia"),
    OGEL("Óleo, Gás e Eletricidade"),
    PQUI("Produtos Químicos e Plásticos"),
    SANE("Saneamento Básico"),
    TIC("TIC"),
    TEXC("Têxtil, Confecção, Couro e Calçados");

    private String nomeSegmento;

    EnumSegmentoEmpresa(String n) { this.nomeSegmento = n; }
}
