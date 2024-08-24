package br.net.rgsul.estoque.entities;

import br.net.rgsul.estoque.dto.BoxDTO;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "boxes")
public class Box {
    @Id
    @Column(nullable = false, unique = true)
    private int id;

    private String name;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;
    private BoxStatus status;

    public Box() {}

    public Box(BoxDTO boxDTO) {
        this.id = boxDTO.id();
        this.name = boxDTO.name();
        this.description = boxDTO.description();
        this.updated = new Timestamp(System.currentTimeMillis());
        this.status = boxDTO.status();
    }

    public void update(BoxDTO boxDTO) {
        this.name = boxDTO.name();
        this.description = boxDTO.description();
        this.updated = new Timestamp(System.currentTimeMillis());
        this.status = boxDTO.status();
    }

    public void setUpdated(){
        this.updated = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public BoxStatus getStatus() {
        return status;
    }
}
