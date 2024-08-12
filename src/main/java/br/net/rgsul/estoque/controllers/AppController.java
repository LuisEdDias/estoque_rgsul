package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.StockCheckDTO;
import br.net.rgsul.estoque.entities.FileDownload;
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

@Controller
@RequestMapping("")
public class AppController {
    @Autowired
    private StockCheckService stockCheckService;

    @GetMapping
    public String form() {
        return "views/form";
    }

    @GetMapping("/index")
    public String index() {
        return "views/index";
    }

    @PostMapping
    public String execute(@ModelAttribute StockCheckDTO stockCheckDTO){
        stockCheckService.stockCheck(stockCheckDTO);
        return "views/index";
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
