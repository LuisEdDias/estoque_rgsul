package br.net.rgsul.estoque.entities;

public enum Status {
    TESTED_OK("TESTADO OK"),
    TO_TEST("TESTAR"),
    FAULTY("TESTADO COM DEFEITO");

    public final String name;

    Status(String name){
        this.name = name;
    }
}
