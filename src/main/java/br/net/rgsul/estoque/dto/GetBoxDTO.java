package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Box;

public record GetBoxDTO(int id, String name, String description) {
    public GetBoxDTO(Box box) {
        this(box.getId(), box.getName(), box.getDescription());
    }
}
