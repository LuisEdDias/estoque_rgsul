package br.net.rgsul.estoque.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record StockCheckDTO(
        MultipartFile principal,
        List<MultipartFile> stock
) {
}
