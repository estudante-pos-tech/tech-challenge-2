package rm349040.techchallenge2.domain.service;

public enum CRUD {


CREATE("Criado(a)"),UPDATE("Atualizado(a)"),DELETE("Excluido(a)"),
LISTAR("Listado(a)"),CREATE_FAIL("FALHA AO CRIAR"),UPDATE_FAIL("FALHA AO ATUALIZAR"),DELETE_FAIL("FALHA AO EXCLUIR"),
LISTAR_FAIL("FALHA AO LISTAR");

    private String action;

    CRUD(String action) {
        this.action = action;
    }

    public String getAction(){
        return action;
    }
}
