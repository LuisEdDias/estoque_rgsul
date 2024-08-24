package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.ItemStatus;

public record ItemDTO(int id, String name, String comment, ItemStatus itemStatus, int boxId) {
}
