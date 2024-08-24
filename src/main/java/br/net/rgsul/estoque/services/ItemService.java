package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.*;
import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.entities.Item;
import br.net.rgsul.estoque.entities.Movement;
import br.net.rgsul.estoque.repositories.BoxRepository;
import br.net.rgsul.estoque.repositories.ItemRepository;
import br.net.rgsul.estoque.repositories.MovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final BoxRepository boxRepository;
    private final MovementRepository movementRepository;

    public ItemService(ItemRepository itemRepository, BoxRepository boxRepository, MovementRepository movementRepository) {
        this.itemRepository = itemRepository;
        this.boxRepository = boxRepository;
        this.movementRepository = movementRepository;
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
        Box box;

        try {
            box = boxRepository.getReferenceById(itemDTO.boxId());
        } catch (RuntimeException e){
            box = null;
        }

        Item item = new Item(itemDTO, box);
        Movement movement = new Movement(item);
        movementRepository.save(movement);
        return new GetItemDTO(itemRepository.save(item));
    }

    public GetItemDTO updateItem(int id, UpdateItemDTO updateItemDTO) {
        Box box;
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            Item itemAux = item.get();
            try {
                box = boxRepository.getReferenceById(updateItemDTO.boxId());
            } catch (RuntimeException e){
                box = null;
            }
            itemAux.update(updateItemDTO, box);
            Movement movement = new Movement(itemAux);
            movementRepository.save(movement);
            return new GetItemDTO(itemRepository.save(itemAux));
        }
        throw new NoSuchElementException("Item not found");
    }

    public void deleteItem(int id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            Item itemAux = item.get();
            Optional<Box> box = boxRepository.findById(itemAux.getBoxId());
            box.ifPresent(Box::setUpdated);
            itemRepository.delete(itemAux);
        }
        throw new NoSuchElementException("Item not found");
    }
}
