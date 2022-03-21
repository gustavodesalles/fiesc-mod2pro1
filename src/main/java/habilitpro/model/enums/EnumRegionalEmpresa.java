package habilitpro.model.enums;

public enum EnumRegionalEmpresa {
    NNE("Norte-Nordeste"),
    OES("Oeste"),
    SUD("Sudeste"),
    CEN("Centro-Norte"),
    VIT("Vale do Itajaí"),
    VIP("Vale do Itapocu"),
    LTS("Litoral Sul"),
    AUR("Alto Uruguai Catarinense"),
    VIM("Vale do Itajaí Mirim"),
    COE("Centro-Oeste"),
    PLN("Planalto Norte"),
    FRI("Foz do Rio Itajaí"),
    SUL("Sul"),
    SRR("Serra Catarinense"),
    EXO("Extremo Oeste"),
    AVI("Alto Vale do Itajaí");

    private String nomeRegional;

    EnumRegionalEmpresa(String nomeRegional) {
        this.nomeRegional = nomeRegional;
    }
}
