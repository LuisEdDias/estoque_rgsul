package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Movement;

import java.time.format.DateTimeFormatter;

public record GetMovementDTO(Long id, String description, String date) {
    public GetMovementDTO(Movement movement) {
        this(
                movement.getId(),
                movement.getDescription(),
                movement.getDate().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
