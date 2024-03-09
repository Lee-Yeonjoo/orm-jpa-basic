package jpabasic.ex1hellojpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)  //cascade 설정 -> 한번의 em.persist로 parent와 child 모두 insert됨. orphanRemoval = true로 하면 리스트에서 지워지면 db에서도 지워짐
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) { //연관관계 편의 메서드
        childList.add(child);
        child.setParent(this);
    }

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

    public List<Child> getChildList() {
        return childList;
    }
}
