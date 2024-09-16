package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.*;
import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.entities.Item;
import br.net.rgsul.estoque.entities.Movement;
import br.net.rgsul.estoque.repositories.BoxRepository;
import br.net.rgsul.estoque.repositories.ItemRepository;
import br.net.rgsul.estoque.repositories.MovementRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final BoxRepository boxRepository;
    private final MovementRepository movementRepository;
    private final ItemsByFileService itemsByFileService;

    public ItemService(ItemRepository itemRepository, BoxRepository boxRepository,
                       MovementRepository movementRepository, ItemsByFileService itemsByFileService) {
        this.itemRepository = itemRepository;
        this.boxRepository = boxRepository;
        this.movementRepository = movementRepository;
        this.itemsByFileService = itemsByFileService;
    }

    public GetItemDTO getItem(int id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return new GetItemDTO(item.get());
        }
        throw new NoSuchElementException("Item not found");
    }

    public List<GetMovementDTO> getItemMovements(int itemId) {
        return movementRepository.findAllByItemId(itemId).stream().map(GetMovementDTO::new).toList();
    }

    public List<GetItemDTO> getAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(GetItemDTO::new).toList();
    }

    public List<GetItemDTO> getAllByBox(Box box) {
        return itemRepository.findAllByBox(box).stream().map(GetItemDTO::new).toList();
    }

    public GetItemDTO createItem(ItemDTO itemDTO) {
        Box box = boxRepository.findById(itemDTO.boxId()).orElseThrow(
                () -> new NoSuchElementException("Box not found")
        );

        Optional<Item> item = itemRepository.findById(itemDTO.id());
        Item itemAux;

        if (item.filter(value -> value.getBox() != null).isPresent()) {
            throw new IllegalArgumentException("Item already exists");
        } else if (item.isPresent()) {
            itemAux = item.get().update(itemDTO, box);
        } else {
            itemAux = new Item(itemDTO, box);
        }

        box.setUpdated();
        boxRepository.save(box);
        movementRepository.save(new Movement(itemAux));
        return new GetItemDTO(itemRepository.save(itemAux));
    }

    public List<ItemDTO> createItemsByFile(ItemsFileDTO fileDTO) {
        List<ItemDTO> itemsFail = new ArrayList<>();
        List<ItemDTO> itemDTOList = itemsByFileService.addItemsByFile(fileDTO);

        if (itemDTOList.isEmpty()) {
            throw new NullPointerException("Wasn't able to create items from file");
        }

        for (ItemDTO itemDTO : itemDTOList) {
            try {
                createItem(itemDTO);
            } catch (RuntimeException e) {
                itemsFail.add(itemDTO);
            }
        }
        return itemsFail;
    }

    public GetItemDTO updateItem(int id, UpdateItemDTO itemDTO) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Item not found"));
        item.update(itemDTO);
        movementRepository.save(new Movement(item));
        return new GetItemDTO(itemRepository.save(item));
    }

    public GetItemDTO movItem(int id, MovementDTO movementDTO) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Item not found")
        );
        Box oldBox = item.getBox();
        Box newBox = null;

        if (movementDTO.boxId() != 0){
            newBox = boxRepository.findById(movementDTO.boxId()).orElseThrow(
                    () -> new NoSuchElementException("Box not found")
            );
        }

        item.move(movementDTO, newBox);
        movementRepository.save(new Movement(item));

        if (oldBox != null){
            oldBox.setUpdated();
            boxRepository.save(oldBox);
        }

        if (newBox != null){
            newBox.setUpdated();
            boxRepository.save(newBox);
        }

        return new GetItemDTO(itemRepository.save(item));
    }
}
