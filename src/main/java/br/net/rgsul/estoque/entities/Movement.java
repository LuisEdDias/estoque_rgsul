package br.net.rgsul.estoque.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "movements")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    private int box;
    private ItemStatus status;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;

    @ManyToOne
    private Item item;

    public Movement(){}

    public Movement(Item item) {
        this.box = item.getBox() == null? 0 : item.getBox().getId();
        this.status = item.getStatus();
        this.item = item;
        this.description = item.getComment() == null? "Adicionado à caixa" : item.getComment();
        this.date = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getDate() {
        return date;
    }

    public Item getItem() {
        return item;
    }

    public int getBox() {
        return box;
    }

    public ItemStatus getStatus() {
        return status;
    }
}
