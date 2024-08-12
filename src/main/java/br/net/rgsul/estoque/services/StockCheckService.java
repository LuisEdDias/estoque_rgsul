package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.entities.FileDownload;
import br.net.rgsul.estoque.entities.StockCheck;
import br.net.rgsul.estoque.dto.StockCheckDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

@Service
public class StockCheckService {
    public void stockCheck(StockCheckDTO stockCheckDTO) {
        StockCheck stockCheck = new StockCheck(stockCheckDTO);
        executeStockCheck(stockCheck);
    }

    private void executeStockCheck(StockCheck stockCheck) {
        int columnToSearch = 1;

        try (InputStream fileInputStream = stockCheck.getPrincipal().getInputStream();
             XSSFWorkbook principalWorkbook = new XSSFWorkbook(fileInputStream)) {

            XSSFSheet sheetPrincipal = principalWorkbook.getSheetAt(0);

            // Cria um estilo de célula com a cor de fundo desejada
            CellStyle style = principalWorkbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);

            for (MultipartFile stock : stockCheck.getStock()) {
                InputStream searchStock = stock.getInputStream();
                System.out.println("Stock found: " + stock.getOriginalFilename());
                XSSFWorkbook searchWorkbook = new XSSFWorkbook(searchStock);
                XSSFSheet sheetStock = searchWorkbook.getSheetAt(0);

                // Itera sobre todas as linhas na coluna especificada
                for (Row rowSearch : sheetStock) {
                    if (rowSearch.getRowNum() == 0) {
                        continue;
                    }

                    Cell searchCell = rowSearch.getCell(columnToSearch);
                    double searchValue = searchCell.getNumericCellValue();

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
                    if (!found){
                        System.out.println(searchValue);
                    }
                }
                searchStock.close();
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
            System.out.println(e.getMessage());;
        }
    }
}
