package br.net.rgsul.estoque.dto;

import br.net.rgsul.estoque.entities.Warehouse;
import org.springframework.web.multipart.MultipartFile;

public record StockCheckDTO(
        MultipartFile principal,
        Warehouse warehouse
) {
}
