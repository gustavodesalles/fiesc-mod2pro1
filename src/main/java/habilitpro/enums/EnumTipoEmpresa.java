package habilitpro.enums;

public enum EnumTipoEmpresa {
    MATRIZ("Matriz"),
    FILIAL("Filial");

    private String nome;

    EnumTipoEmpresa(String nome) {
        this.nome = nome.toUpperCase();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
