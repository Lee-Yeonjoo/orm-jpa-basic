package jpa.exercise.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity //자식엔티티에도 엔티티 어노테이션 붙여야함!
@DiscriminatorValue("ALBUM")
@Setter @Getter
public class Album extends Item{ //Item이 BaseEntity 상속받음.

    //당연히 id 기본키 필요없음
    private String artist;
    private String etc;
}
