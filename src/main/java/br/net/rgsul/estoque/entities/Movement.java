package br.net.rgsul.estoque.entities;

import jakarta.persistence.*;

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

    @ManyToOne(cascade = CascadeType.ALL)
    private Item item;

    public Movement(){}

    public Movement(Item item) {
        this.box = item.getBoxId();
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
