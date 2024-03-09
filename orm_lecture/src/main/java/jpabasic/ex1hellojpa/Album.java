package jpabasic.ex1hellojpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("A")  //디폴트는 엔티티명. 디타입 컬럼에 들어갈 디타입의 이름을 설정.
public class Album extends Item{

    private String artist;
}
