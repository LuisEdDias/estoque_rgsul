package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Movement;

import java.time.format.DateTimeFormatter;

public record GetMovementDTO(String description,String status, int box, String date) {
    public GetMovementDTO(Movement movement) {
        this(
                movement.getDescription(),
                movement.getStatus().nameStr,
                movement.getBox(),
                movement.getDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }
}
