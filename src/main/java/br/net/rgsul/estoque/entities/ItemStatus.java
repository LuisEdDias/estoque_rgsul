package br.net.rgsul.estoque.entities;

public enum ItemStatus {
    TESTED_OK("TESTADO OK"),
    TO_TEST("TESTAR"),
    FAULTY("DEFEITO");

    public final String nameStr;

    ItemStatus(String nameStr){
        this.nameStr = nameStr;
    }
}
