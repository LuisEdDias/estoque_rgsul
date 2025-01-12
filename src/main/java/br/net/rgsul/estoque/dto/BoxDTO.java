package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.BoxStatus;
import br.net.rgsul.estoque.entities.Warehouse;

public record BoxDTO(
        int id,
        String name,
        String description,
        BoxStatus status,
        Warehouse warehouse) {
}
