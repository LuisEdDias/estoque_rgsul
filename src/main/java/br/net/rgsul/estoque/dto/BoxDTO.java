package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.BoxStatus;

public record BoxDTO(
        int id,
        String name,
        String description,
        BoxStatus status) {
}
