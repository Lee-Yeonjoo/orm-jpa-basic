package jpabasic.ex1hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity //이걸 넣어야 jpa가 관리함.
//@Table(name = "USER") //테이블명이 USER인 테이블과 매핑
public class Member {

    @Id //jpa에게 pk가 뭔지 알려줘야함.
    private Long id;

    //@Column(name = "username") //insert 쿼리가 username으로 나간다.
    private String name;

    public Member() {

    } //기본생성자가 있어야한다!

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
