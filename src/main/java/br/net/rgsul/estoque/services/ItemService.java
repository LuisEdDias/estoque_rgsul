package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.*;
import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.entities.Item;
import br.net.rgsul.estoque.entities.Movement;
import br.net.rgsul.estoque.entities.Warehouse;
import br.net.rgsul.estoque.repositories.BoxRepository;
import br.net.rgsul.estoque.repositories.ItemRepository;
import br.net.rgsul.estoque.repositories.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Item> items = itemRepository.findAllBySaved(true);
        return items.stream().map(GetItemDTO::new).toList();
    }

    public List<GetItemDTO> getAllByWarehouse(Warehouse warehouse) {
        List<Item> items = itemRepository.findAllByBox_Warehouse(warehouse);
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

        itemRepository.save(itemAux);
        box.setUpdated();
        boxRepository.save(box);
        movementRepository.save(new Movement(itemAux));
        return new GetItemDTO(itemAux);
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

    @Transactional
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

        item.move(movementDTO.status(), movementDTO.comment(), newBox);
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

    @Transactional
    public List<GetItemDTO> movItems(MoveAllDTO moveAllDTO) {
        // Busca todos os itens pelos IDs fornecidos
        List<Item> items = itemRepository.findAllByIdIn(moveAllDTO.ids());

        if (items.size() != moveAllDTO.ids().size()) {
            throw new NoSuchElementException("Some items were not found");
        }

        // Determinação da nova caixa (uma única busca no banco)
        Box newBox = null;

        if (moveAllDTO.boxId() != 0){
            newBox = boxRepository.findById(moveAllDTO.boxId()).orElseThrow(
                    () -> new NoSuchElementException("Box not found")
            );
        }

        // Movimentação dos itens e registro das movimentações
        List<Movement> movements = new ArrayList<>();
        List<Box> boxesToUpdate = new ArrayList<>();

        for (Item item : items) {
            Box oldBox = item.getBox();

            // Movimenta o item
            item.move(null, moveAllDTO.comment(), newBox);

            // Cria o registro de movimentação
            movements.add(new Movement(item));

            // Adiciona as caixas para atualização, evitando duplicatas
            if (oldBox != null && !boxesToUpdate.contains(oldBox)) {
                boxesToUpdate.add(oldBox);
            }
            if (newBox != null && !boxesToUpdate.contains(newBox)) {
                boxesToUpdate.add(newBox);
            }
        }

        // Salva as movimentações em lote
        movementRepository.saveAll(movements);

        // Atualiza as caixas
        for (Box box : boxesToUpdate) {
            box.setUpdated();
            boxRepository.save(box);
        }

        // Salva os itens atualizados e converte para DTOs
        List<Item> updatedItems = itemRepository.saveAll(items);
        return updatedItems.stream()
                .map(GetItemDTO::new)
                .toList();
    }

    @Transactional
    public void deleteItem(int id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
        itemRepository.delete(item);
    }

    @Transactional
    public void removeItemFromBox(Box box) {
        itemRepository.updateItemsFromDeletedBox(box);
    }
}
