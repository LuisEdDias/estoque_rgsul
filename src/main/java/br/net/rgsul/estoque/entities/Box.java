package br.net.rgsul.estoque.entities;

import br.net.rgsul.estoque.dto.BoxDTO;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "boxes")
public class Box {
    @Id
    private int id;

    private String name;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updated;

    public Box() {}

    public Box(BoxDTO boxDTO) {
        this.id = boxDTO.id();
        this.name = boxDTO.name();
        this.description = boxDTO.description();
        this.updated = new Timestamp(System.currentTimeMillis());
    }

    public void update(BoxDTO boxDTO) {
        this.name = boxDTO.name();
        this.description = boxDTO.description();
        this.updated = new Timestamp(System.currentTimeMillis());
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

    @Override
    public String toString() {
        return
                "ID: " + id +
                ", Name: " + name +
                ", Description: " + description +
                ", Updated: " + updated.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
