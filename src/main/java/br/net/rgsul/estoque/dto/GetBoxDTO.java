package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Box;

import java.time.format.DateTimeFormatter;

public record GetBoxDTO(int id, String name, String description, String updated, String status, String warehouse) {
    public GetBoxDTO(Box box) {
        this(
                box.getId(),
                box.getName(),
                box.getDescription(),
                box.getUpdated().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                box.getStatus().nameStr,
                box.getWarehouse().nameStr
        );
    }
}
