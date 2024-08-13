package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Status;

public record ItemDTO(int id, String name, String comment, Status status) {
}
