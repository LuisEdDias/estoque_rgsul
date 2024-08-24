package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.ItemStatus;

public record UpdateItemDTO(String name, String comment, ItemStatus itemStatus, String saved, int boxId) {
}
