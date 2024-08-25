package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.ItemStatus;

public record MovementDTO(String comment, ItemStatus status, int boxId) {
}
