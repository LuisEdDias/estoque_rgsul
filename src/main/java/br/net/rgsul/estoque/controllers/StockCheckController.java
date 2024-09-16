package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.StockCheckDTO;
import br.net.rgsul.estoque.entities.FileDownload;
import br.net.rgsul.estoque.entities.Item;
import br.net.rgsul.estoque.repositories.ItemRepository;
import br.net.rgsul.estoque.services.ItemService;
import br.net.rgsul.estoque.services.StockCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("check")
public class StockCheckController {
    @Autowired
    private StockCheckService stockCheckService;
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("completed")
    public String index() {
        return "views/completed";
    }

    @PostMapping
    public String execute(@ModelAttribute StockCheckDTO stockCheckDTO){
        List<Item> items = itemRepository.findAllBySaved(true);
        stockCheckService.stockCheck(stockCheckDTO, items);
        return "views/completed";
    }

    @GetMapping("download")
    public ResponseEntity<Resource> getFile() {
        File file = FileDownload.file;

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(FileDownload.file);
        String fileName = FileDownload.file.getName();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
