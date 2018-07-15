package br.com.tbsa.repository;

public class PerfilRepositoryHelper {

    private String id;
    private Boolean selecao;

    public PerfilRepositoryHelper() {
    }

    public PerfilRepositoryHelper(String id, Boolean selecao) {
        this.id = id;
        this.selecao = selecao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSelecao() {
        return selecao;
    }

    public void setSelecao(Boolean selecao) {
        this.selecao = selecao;
    }

    @Override
    public String toString() {
        return id + ";" + selecao + "#";
    }

}
