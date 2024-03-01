package jpabasic.ex1hellojpa;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

import javax.swing.undo.AbstractUndoableEdit;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") //Member클래스의 team과 매핑되어있단 뜻. team에 의해 관리됨. 즉, 읽기만 가능.
    private List<Member> members = new ArrayList<>(); //arraylist로 초기화하는게 nullpoint가 안뜨므로 관례

    public void addMember(Member member) { //여기에서도 연관관계 편의 메서드를 만들 수 있다. -> 한쪽에만 만들기.
        member.setTeam(this);
        members.add(member);
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

    public List<Member> getMembers() {
        return members;
    }

  /*  @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", members=" + members +
                '}';
    }*/
}
