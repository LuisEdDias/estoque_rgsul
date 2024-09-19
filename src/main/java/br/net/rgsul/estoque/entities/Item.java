package br.net.rgsul.estoque.entities;

import br.net.rgsul.estoque.dto.ItemDTO;
import br.net.rgsul.estoque.dto.MovementDTO;
import br.net.rgsul.estoque.dto.UpdateItemDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @Column(nullable = false, unique = true)
    private int id;

    private String name;
    private String comment;
    private ItemStatus itemStatus;
    private boolean saved;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;

    @ManyToOne
    private Box box;

    public Item() {
    }

    public Item(ItemDTO itemDTO, Box box) {
        this.id = itemDTO.id();
        this.name = itemDTO.name();
        this.comment = itemDTO.comment();
        this.itemStatus = itemDTO.status();
        this.saved = true;
        this.updated = new Timestamp(System.currentTimeMillis());
        this.box = box;
    }

    public Item update(ItemDTO itemDTO, Box box) {
        this.name = itemDTO.name();
        this.comment = itemDTO.comment();
        this.itemStatus = itemDTO.status();
        this.updated = new Timestamp(System.currentTimeMillis());
        this.box = box;
        return this;
    }

    public void update(UpdateItemDTO itemDTO) {
        this.name = itemDTO.name();
        this.comment = itemDTO.comment();
        this.itemStatus = itemDTO.status();
        this.updated = new Timestamp(System.currentTimeMillis());
    }

    public void move(MovementDTO movementDTO, Box box) {
        this.updated = new Timestamp(System.currentTimeMillis());
        this.itemStatus = movementDTO.status();
        this.comment = movementDTO.comment();
        this.box = box;
        saved = box != null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemStatus getStatus() {
        return itemStatus;
    }

    public String getComment() {
        return comment;
    }

    public boolean isSaved() {
        return saved;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public Box getBox() {
        return box;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " Name: " + name +
                " Comment: " + comment +
                " Updated: " + updated.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                " Box: " + box.getId();
    }
}
