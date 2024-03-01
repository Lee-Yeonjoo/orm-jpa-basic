package jpabasic.ex1hellojpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id @GeneratedValue
    private Long id;

    private String name;

    /*@ManyToMany(mappedBy = "products")  //다대다 양방향
    private List<Member> members = new ArrayList<>();*/

    @OneToMany(mappedBy = "product") //실무에서의 다대다
    private List<MemberProduct> memberProducts = new ArrayList<>();

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
