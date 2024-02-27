package jpabasic.ex1hellojpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity //이걸 넣어야 jpa가 관리함.
//@Table(name = "USER") //테이블명이 USER인 테이블과 매핑
public class Member {

    /*@Id //jpa에게 pk가 뭔지 알려줘야함.
    private Long id;

    //@Column(name = "username") //insert 쿼리가 username으로 나간다.
    @Column(unique = true, length = 10) //유니크 제약 조건 지정, 길이 제한 -> 런타임에 영향x, 그냥 DDL 생성에만 영향 준다.
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
    }*/

    @Id
    private Long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    //@Enumerated(EnumType.ORDINAL) //이넘 순서를 저장하는거라 쓰면 안됨.
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    @Lob
    private String description;

    @Transient
    private int temp; //db와 관계없는 필드. 메모리에서만 쓰는 필드.

    public Member() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
