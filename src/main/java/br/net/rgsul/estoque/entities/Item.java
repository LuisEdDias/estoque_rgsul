package br.net.rgsul.estoque.entities;

import br.net.rgsul.estoque.dto.ItemDTO;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "items")
public class Item {
    @Id
    private int id;

    private String name;
    private String comment;
    private Status status;
    private boolean saved;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;

    public Item() {}

    public Item(ItemDTO itemDTO) {
        this.id = itemDTO.id();
        this.name = itemDTO.name();
        this.comment = itemDTO.comment();
        this.status = itemDTO.status();
        this.saved = true;
        this.updated = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
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

    @Override
    public String toString() {
        return "ID: " + id +
                ", Name: " + name +
                ", Comment: " + comment +
                ", Status: " + status +
                ", Inside Box: " + saved +
                ", Updated: " + updated.toLocalDateTime();
    }
}
