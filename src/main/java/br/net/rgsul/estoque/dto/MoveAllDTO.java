package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.ItemStatus;

import java.util.List;

public record MoveAllDTO(List<Integer> ids, String comment, ItemStatus status, int boxId) {
}
