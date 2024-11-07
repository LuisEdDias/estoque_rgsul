package br.net.rgsul.estoque.dto;

import java.util.List;

public record GetBoxCheckDTO(List<GetItemDTO> itemsNotFound, List<Integer> unboxedItems) {
}
