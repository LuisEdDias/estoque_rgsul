package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Item;

import java.time.format.DateTimeFormatter;

public record GetItemDTO(int id, String name, String comment, String status, String saved, String updated, int boxId) {
    public GetItemDTO(Item item){
        this(
                item.getId(),
                item.getName(),
                item.getComment(),
                item.getStatus().nameStr,
                item.isSaved()? "NA CAIXA" : "NA RUA",
                item.getUpdated().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                item.getBox() == null? 0 : item.getBox().getId()
        );
    }
}
