package br.net.rgsul.estoque.entities;

public enum Warehouse {
    PELOTAS("PELOTAS"),
    PAI_ROGER("PAI ROGER");

    public final String nameStr;

    Warehouse(String nameStr){
        this.nameStr = nameStr;
    }
}
