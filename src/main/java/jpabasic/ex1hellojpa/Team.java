package jpabasic.ex1hellojpa;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

import javax.swing.undo.AbstractUndoableEdit;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
