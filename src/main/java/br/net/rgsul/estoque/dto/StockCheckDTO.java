package br.net.rgsul.estoque.dto;

import org.springframework.web.multipart.MultipartFile;

public record StockCheckDTO(
        MultipartFile principal
) {
}
