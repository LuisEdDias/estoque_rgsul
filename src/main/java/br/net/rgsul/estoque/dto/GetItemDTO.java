package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Item;

import java.time.format.DateTimeFormatter;

public record GetItemDTO(int id, String name, String comment, String status, String saved, String updated) {
    public GetItemDTO(Item item) {
        this(
                item.getId(),
                item.getName(),
                item.getComment(),
                item.getStatus().name(),
                item.isSaved()? "Na Caixa" : "Na Rua",
                item.getUpdated().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
