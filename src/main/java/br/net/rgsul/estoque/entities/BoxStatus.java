package br.net.rgsul.estoque.entities;

public enum BoxStatus {
    EMPTY("MUITO ESPAÇO"),
    SOME_SPACE("POUCO ESPAÇO"),
    FULL("CHEIO");

    public final String nameStr;

    BoxStatus(String nameStr){
        this.nameStr = nameStr;
    }
}
