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

    public Box findById(int id) {
        Optional<Box> box = boxRepository.findById(id);
        if (box.isPresent()) {
            return box.get();
        }
        throw new NoSuchElementException();
    }

    public List<Box> findAll() {
        return boxRepository.findAll();
    }

    public Box save(BoxDTO boxDTO) {
        Box box = new Box(boxDTO);
        return boxRepository.save(box);
    }

    public Box update(BoxDTO boxDTO) {
        Optional<Box> box = boxRepository.findById(boxDTO.id());
        if (box.isPresent()) {
            Box boxAux = box.get();
            boxAux.update(boxDTO);
            return boxRepository.save(boxAux);
        }
        throw new NoSuchElementException();
    }

    public void delete(int id) {
        boxRepository.deleteById(id);
    }
}
