package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.*;
import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.entities.BoxFileDownload;
import br.net.rgsul.estoque.repositories.BoxRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class BoxService {
    private final BoxRepository boxRepository;

    private final ItemService itemService;

    public BoxService(BoxRepository boxRepository, ItemService itemService) {
        this.boxRepository = boxRepository;
        this.itemService = itemService;
    }

    public List<GetBoxDTO> findAll() {
        return boxRepository.findAll().stream().map(GetBoxDTO::new).toList();
    }

    public GetBoxDTO getBoxById(int id) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            return new GetBoxDTO(box.get());
        }
        throw new NoSuchElementException();
    }

    public void save(BoxDTO boxDTO) {
        Box box = new Box(boxDTO);
        new GetBoxDTO(boxRepository.save(box));
    }

    public GetBoxDTO update(int id, BoxDTO boxDTO) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            Box boxAux = box.get();
            boxAux.update(boxDTO);
            return new GetBoxDTO(boxRepository.save(boxAux));
        }
        throw new NoSuchElementException();
    }

    public GetBoxDTO move(Integer id, WarehouseDTO warehouseDTO) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            Box boxAux = box.get();
            boxAux.move(warehouseDTO);
            return new GetBoxDTO(boxRepository.save(boxAux));
        }
        throw new NoSuchElementException();
    }

    public void delete(int id) {
        Box box = boxRepository.findById(id).orElseThrow(NoSuchElementException::new);
        itemService.removeItemFromBox(box);
        boxRepository.delete(box);
    }

    public List<GetItemDTO> findAllItems(int id) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            return itemService.getAllByBox(box.get());
        }
        throw new NoSuchElementException("Box not found");
    }

    public GetBoxCheckDTO boxCheck(int id, BoxCheckDTO boxCheckDTO) {
        HashMap<Integer, GetItemDTO> itemsMap = new HashMap<>();
        List<Integer> unboxedItems = new ArrayList<>();

        for (GetItemDTO getItemDTO : findAllItems(id)) {
            itemsMap.put(getItemDTO.id(), getItemDTO);
        }

        boxCheckDTO.ids().forEach(item ->{
            GetItemDTO aux = itemsMap.remove(item);
            if (aux == null){
                unboxedItems.add(item);
            }
        });

        return new GetBoxCheckDTO(itemsMap.values().stream().toList(), unboxedItems);
    }

    public void exportBox(int id){
        List<GetItemDTO> items = findAllItems(id);
        BoxFileDownload.file = new File("CAIXA " + id + ".xlsx");

        try (FileOutputStream outputStream = new FileOutputStream(BoxFileDownload.file);
             XSSFWorkbook workbook = createItemsToCheckWorkbook(items)) {

            workbook.write(outputStream);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private XSSFWorkbook createItemsToCheckWorkbook(List<GetItemDTO> items) throws IOException {
        InputStream exportFileInputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("EXPORTAR CAIXA.xlsx");

        assert exportFileInputStream != null;
        XSSFWorkbook workbook = new XSSFWorkbook(exportFileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        int rowIndex = 1;

        for (GetItemDTO item : items) {
            Row row = sheet.createRow(rowIndex++);
            createCell(row, 0, item.name(), style);
            createCell(row, 1, (double) item.id(), style);
            createCell(row, 2, item.comment(), style);
            createCell(row, 3, item.status(), style);
            createCell(row, 4, item.updated(), style);
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
}
