package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Status;

public record UpdateItemDTO(String name, String comment, Status status, String saved, int boxId) {
}
