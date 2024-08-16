package br.net.rgsul.estoque.entities;

import br.net.rgsul.estoque.dto.ItemDTO;
import br.net.rgsul.estoque.dto.UpdateItemDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @Column(nullable = false, unique = true)
    private int id;

    private String name;
    private String comment;
    private Status status;
    private boolean saved;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Box box;

    public Item() {}

    public Item(ItemDTO itemDTO, Box box) {
        this.id = itemDTO.id();
        this.name = itemDTO.name();
        this.comment = itemDTO.comment();
        this.status = itemDTO.status();
        this.saved = true;
        this.updated = new Timestamp(System.currentTimeMillis());
        this.box = box;
        box.setUpdated();
        new Movement(this);
    }

    public void update(UpdateItemDTO itemDTO, Box box) {
        this.name = itemDTO.name();
        this.comment = itemDTO.comment();
        this.status = itemDTO.status();
        this.saved = Boolean.parseBoolean(itemDTO.saved());
        this.updated = new Timestamp(System.currentTimeMillis());
        this.box = box;
        box.setUpdated();
        new Movement(this);
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

    public int getBoxId() {
        return box == null? 0 : box.getId();
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
