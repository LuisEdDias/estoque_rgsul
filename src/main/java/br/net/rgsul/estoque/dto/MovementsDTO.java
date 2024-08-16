package br.net.rgsul.estoque.dto;

import java.util.List;

public record MovementsDTO(int id, String name, List<GetMovementDTO> movements) {
}
