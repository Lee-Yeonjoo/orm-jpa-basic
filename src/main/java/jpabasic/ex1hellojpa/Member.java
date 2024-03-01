package jpabasic.ex1hellojpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/*@Entity //이걸 넣어야 jpa가 관리함.
//@Table(name = "USER") //테이블명이 USER인 테이블과 매핑
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50 )
public class Member {

    *//*@Id //jpa에게 pk가 뭔지 알려줘야함.
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
    }*//*

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
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

    public Long getId() {
        return id;
    }
}*/

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

  /*  @Column(name = "TEAM_ID")
    private Long teamId;*/
    @ManyToOne //멤버 입장에서 many이니까. 어떤 연관관계인지. 다대일관계
    @JoinColumn(name = "TEAM_ID") //조인하는 컬럼이 뭔지 적어준다. db와 연결됨. 외래키를 관리! 연관관계의 주인.
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /*public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }*/

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); //이 코드를 추가해 연관관계 편의 메서드를 만들기!
        //이미 멤버리스트에 자기자신이 있는지 체크하는 로직이 필요할 때도 있다.
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    /*@Override
    public String toString() { //team의 toString()을 호출하는데 여기서도 members의 toString()을 호출해서 무한루프에 빠짐.
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", team=" + team +
                '}';
    }*/
}
