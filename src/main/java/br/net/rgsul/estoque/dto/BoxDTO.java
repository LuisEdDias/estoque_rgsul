package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Box;

public record BoxDTO(int id, String name, String description) {
    public BoxDTO(Box box) {
        this(box.getId(), box.getName(), box.getDescription());
    }
}
