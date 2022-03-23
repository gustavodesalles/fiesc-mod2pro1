package habilitpro.model.enums;

public enum EnumTipoEmpresa {
    MATRIZ("Matriz"),
    FILIAL("Filial");

    private String nome;

    EnumTipoEmpresa(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
