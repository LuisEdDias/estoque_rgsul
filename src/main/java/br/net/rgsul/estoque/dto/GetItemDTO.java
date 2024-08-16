package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Item;

import java.time.format.DateTimeFormatter;

public record GetItemDTO(int id, String name, String comment, String status, String saved, String updated, int boxId) {
    public GetItemDTO(Item item){
        this(
                item.getId(),
                item.getName(),
                item.getComment(),
                item.getStatus().name(),
                item.isSaved()? "NA CAIXA" : "NA RUA",
                item.getUpdated().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                item.getBoxId()
        );
    }
}
