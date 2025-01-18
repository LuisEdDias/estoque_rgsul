package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.entities.StockCheckFileDownload;
import br.net.rgsul.estoque.entities.Item;
import br.net.rgsul.estoque.entities.StockCheck;
import br.net.rgsul.estoque.dto.StockCheckDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockCheckService {
    public void stockCheck(StockCheckDTO stockCheckDTO, List<Item> items) {
        StockCheck stockCheck = new StockCheck(stockCheckDTO);
        executeStockCheck(stockCheck, items);
    }

    private void executeStockCheck(StockCheck stockCheck, List<Item> items) {
        StockCheckFileDownload.file = null;
        StockCheckFileDownload.itemsToBeChecked = null;

        try (InputStream fileInputStream = stockCheck.getPrincipal().getInputStream();
             XSSFWorkbook principalWorkbook = new XSSFWorkbook(fileInputStream)) {

            List<Item> itemsToBeChecked = new ArrayList<>();
            XSSFSheet sheetPrincipal = principalWorkbook.getSheetAt(0);

            int columToSearch = 2;
            Row header = sheetPrincipal.getRow(0);
            for (Cell cell : header) {
                if (cell.getStringCellValue().toLowerCase().contains("id")){
                    columToSearch = cell.getColumnIndex();
                }
            }

            // Criação do índice para busca rápida
            Map<Double, Row> rowIndex = buildRowIndex(sheetPrincipal, columToSearch);

            // Criação do estilo de destaque
            CellStyle style = createCellStyle(principalWorkbook, IndexedColors.BRIGHT_GREEN);

            for (Item item : items) {
                Row row = rowIndex.get((double) item.getId());
                if (row != null) {
                    row.getCell(2).setCellStyle(style); // Destaca a célula encontrada
                } else {
                    itemsToBeChecked.add(item); // Adiciona item não encontrado
                }
            }

            if (!itemsToBeChecked.isEmpty()) {
                XSSFWorkbook toCheckWorkbook = createItemsToCheckWorkbook(itemsToBeChecked);
                StockCheckFileDownload.itemsToBeChecked = new File("ITENS À VERIFICAR.xlsx");
                saveWorkbookToFile(toCheckWorkbook, StockCheckFileDownload.itemsToBeChecked);
            }

            StockCheckFileDownload.file = new File("PEL - FECHAMENTO ESTOQUE.xlsx");
            saveWorkbookToFile(principalWorkbook, StockCheckFileDownload.file);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Map<Double, Row> buildRowIndex(XSSFSheet sheet, int searchColumn) {
        Map<Double, Row> rowMap = new HashMap<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(searchColumn);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    rowMap.put(cell.getNumericCellValue(), row);
                }
            }
        }
        return rowMap;
    }

    private CellStyle createCellStyle(XSSFWorkbook workbook, IndexedColors color) {
        CellStyle style = workbook.createCellStyle();
        if (color != null) {
            style.setFillForegroundColor(color.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private XSSFWorkbook createItemsToCheckWorkbook(List<Item> itemsToBeChecked) throws IOException {
        InputStream toCheckFileInputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("ITENS À VERIFICAR.xlsx");

        assert toCheckFileInputStream != null;
        XSSFWorkbook workbook = new XSSFWorkbook(toCheckFileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        CellStyle style = createCellStyle(workbook, null);

        int rowIndex = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Item item : itemsToBeChecked) {
            Row row = sheet.createRow(rowIndex++);
            createCell(row, 0, item.getName(), style);
            createCell(row, 1, (double) item.getId(), style);
            createCell(row, 2, item.getComment(), style);
            createCell(row, 3, (double) item.getBox().getId(), style);
            createCell(row, 4, item.getUpdated().toLocalDateTime().format(formatter), style);
        }

        return workbook;

    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else {
            cell.setCellValue((double) value);
        }
        cell.setCellStyle(style);
    }

    private void saveWorkbookToFile(XSSFWorkbook workbook, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        }
    }
}
