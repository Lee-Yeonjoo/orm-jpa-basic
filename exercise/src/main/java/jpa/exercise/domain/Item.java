package jpa.exercise.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
/*@Builder
@NoArgsConstructor
@AllArgsConstructor*/
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //싱글테이블 전략
@DiscriminatorColumn(name = "DTYPE")
@Getter
@Setter
public abstract class Item extends BaseEntity { //Item만 단독으로 저장하는 경우가 없다면 추상클래스로 만든다. -> 빌더패턴 관련 롬복 사용 못함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    /*@ManyToMany(mappedBy = "items") //다대다 매핑. 연관관계의 주인이 items
    private List<Category> categories = new ArrayList<>();*/
}
