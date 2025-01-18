package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.ItemDTO;
import br.net.rgsul.estoque.dto.ItemsFileDTO;
import br.net.rgsul.estoque.entities.ItemStatus;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemsByFileService {
    public List<ItemDTO> addItemsByFile(ItemsFileDTO fileDTO) {
        List<ItemDTO> items = new ArrayList<>();
        String comment = fileDTO.comment();
        int boxId = fileDTO.boxId();
        int colId = 1;
        int colName = 2;
        int colStatus = 3;

        try (InputStream itemsFile = fileDTO.file().getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(itemsFile)
        ) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                try {
                    String name = row.getCell(colName).getStringCellValue();
                    int id = (int) row.getCell(colId).getNumericCellValue();
                    ItemStatus status = getStatus(row.getCell(colStatus).getStringCellValue());
                    items.add(new ItemDTO(id, name, comment, status, boxId));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Não foi possível cadastrar os itens!");
        }
        return items;
    }

    private ItemStatus getStatus(String stt) {
        return switch (stt) {
            case "TESTAR" -> ItemStatus.TO_TEST;
            case "TESTADO OK" -> ItemStatus.TESTED_OK;
            case "DEFEITO" -> ItemStatus.FAULTY;
            default -> null;
        };
    }
}
