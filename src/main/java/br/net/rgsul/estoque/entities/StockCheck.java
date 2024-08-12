package br.net.rgsul.estoque.entities;

import br.net.rgsul.estoque.dto.StockCheckDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class StockCheck {
    private final MultipartFile principal;

    private final List<MultipartFile> stock;

    public StockCheck(StockCheckDTO stockCheckDTO) {
        this.principal = stockCheckDTO.principal();
        this.stock = stockCheckDTO.stock();
    }

    public MultipartFile getPrincipal() {
        return principal;
    }

    public List<MultipartFile> getStock() {
        return stock;
    }
}
