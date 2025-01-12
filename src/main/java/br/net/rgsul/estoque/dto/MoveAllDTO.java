package br.net.rgsul.estoque.dto;

import java.util.List;

public record MoveAllDTO(List<Integer> ids, String comment, int boxId) {
}
