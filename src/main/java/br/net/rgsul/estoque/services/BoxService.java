package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.BoxDTO;
import br.net.rgsul.estoque.dto.GetBoxDTO;
import br.net.rgsul.estoque.dto.GetItemDTO;
import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.repositories.BoxRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public GetBoxDTO save(BoxDTO boxDTO) {
        Box box = new Box(boxDTO);
        return new GetBoxDTO(boxRepository.save(box));
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

    public void delete(int id) {
        boxRepository.deleteById(id);
    }

    public List<GetItemDTO> findAllItems(int id) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            return itemService.getAllByBox(box.get());
        }
        throw new NoSuchElementException("Box not found");
    }
}
