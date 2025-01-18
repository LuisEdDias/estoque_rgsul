package br.net.rgsul.estoque.controllers;

import br.net.rgsul.estoque.dto.StockCheckDTO;
import br.net.rgsul.estoque.entities.StockCheckFileDownload;
import br.net.rgsul.estoque.entities.Item;
import br.net.rgsul.estoque.repositories.ItemRepository;
import br.net.rgsul.estoque.services.StockCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public String execute(@ModelAttribute StockCheckDTO stockCheckDTO) {
        List<Item> items = itemRepository.findAllByBox_Warehouse(stockCheckDTO.warehouse());
        stockCheckService.stockCheck(stockCheckDTO, items);
        return "views/completed";
    }

    @GetMapping("download")
    public ResponseEntity<Resource> getFile() throws IOException {
        File file = StockCheckFileDownload.file;
        File fileToCheck = StockCheckFileDownload.itemsToBeChecked;
        Timestamp date = new Timestamp(System.currentTimeMillis());

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        if (fileToCheck == null) {
            Resource resource = new FileSystemResource(file);
            String fileName = file.getName();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            // Adicionando o primeiro arquivo ao ZIP
            addFileToZip(file, zipOut);

            // Adicionando o segundo arquivo ao ZIP
            addFileToZip(fileToCheck, zipOut);

        }

        // Preparando o recurso para download
        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"conferencia.zip\"")
                .body(resource);

    }

    // Método auxiliar para adicionar arquivos ao ZIP
    private void addFileToZip(File file, ZipOutputStream zipOut) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }

            zipOut.closeEntry();
        }
    }
}
