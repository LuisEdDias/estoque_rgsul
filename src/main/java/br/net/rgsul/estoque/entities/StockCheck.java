package br.net.rgsul.estoque.entities;

import br.net.rgsul.estoque.dto.StockCheckDTO;
import org.springframework.web.multipart.MultipartFile;

public class StockCheck {
    private final MultipartFile principal;

    public StockCheck(StockCheckDTO stockCheckDTO) {
        this.principal = stockCheckDTO.principal();
    }

    public MultipartFile getPrincipal() {
        return principal;
    }
}
