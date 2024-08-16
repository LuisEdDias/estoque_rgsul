package br.net.rgsul.estoque.services;

import br.net.rgsul.estoque.dto.BoxDTO;
import br.net.rgsul.estoque.entities.Box;
import br.net.rgsul.estoque.repositories.BoxRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BoxService {
    private final BoxRepository boxRepository;

    public BoxService(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    public BoxDTO findById(int id) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            return new BoxDTO(box.get());
        }
        throw new NoSuchElementException();
    }

    public List<BoxDTO> findAll() {
        return boxRepository.findAll().stream().map(BoxDTO::new).toList();
    }

    public BoxDTO save(BoxDTO boxDTO) {
        Box box = new Box(boxDTO);
        return new BoxDTO(boxRepository.save(box));
    }

    public BoxDTO update(int id, BoxDTO boxDTO) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            Box boxAux = box.get();
            boxAux.update(boxDTO);
            return new BoxDTO(boxRepository.save(boxAux));
        }
        throw new NoSuchElementException();
    }

    public void delete(int id) {
        boxRepository.deleteById(id);
    }
}
