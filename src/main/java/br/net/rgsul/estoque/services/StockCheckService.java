package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.entities.FileDownload;
import br.net.rgsul.estoque.entities.Item;
import br.net.rgsul.estoque.entities.StockCheck;
import br.net.rgsul.estoque.dto.StockCheckDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class StockCheckService {
    public void stockCheck(StockCheckDTO stockCheckDTO, List<Item> items) {
        StockCheck stockCheck = new StockCheck(stockCheckDTO);
        executeStockCheck(stockCheck, items);
    }

    private void executeStockCheck(StockCheck stockCheck, List<Item> items) {
        try (InputStream fileInputStream = stockCheck.getPrincipal().getInputStream();
             XSSFWorkbook principalWorkbook = new XSSFWorkbook(fileInputStream)) {

            XSSFSheet sheetPrincipal = principalWorkbook.getSheetAt(0);

            // Cria um estilo de célula com a cor de fundo desejada
            CellStyle style = principalWorkbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);

            // Itera sobre todas as linhas na coluna especificada
            for (Item item : items) {
                double searchValue = item.getId();

                int rowIndex = 0;
                boolean found = false;

                do {
                    Row row = sheetPrincipal.getRow(rowIndex);
                    Cell cell = row.getCell(2);
                    if (cell.getCellType() == CellType.NUMERIC) {
                        double value = cell.getNumericCellValue();
                        if (value == searchValue) {
                            cell.setCellStyle(style);
                            found = true;
                        }
                    }
                    rowIndex++;

                } while (!found && rowIndex <= sheetPrincipal.getLastRowNum());
                if (!found) {
                    System.out.println(item);
                }
            }

            // Grava as alterações de volta no arquivo
            FileDownload.file = new File("PEL - FECHAMENTO ESTOQUE.xlsx");
            try (FileOutputStream fileOutputStream = new FileOutputStream(FileDownload.file) {
            }) {
                principalWorkbook.write(fileOutputStream);
                fileOutputStream.close();
                System.out.println("Principal " + FileDownload.file.getName());
                principalWorkbook.close();
                System.out.println("Success!!");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            ;
        }
    }
}
