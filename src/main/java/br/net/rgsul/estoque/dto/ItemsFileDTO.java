package br.net.rgsul.estoque.dto;

import org.springframework.web.multipart.MultipartFile;

public record ItemsFileDTO(
        String comment,
        int boxId,
        MultipartFile file) {
}
