package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.*;
import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.repositories.BoxRepository;
import org.springframework.stereotype.Service;

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
}
