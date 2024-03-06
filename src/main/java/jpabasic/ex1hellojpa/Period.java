package jpabasic.ex1hellojpa;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isWork() { //해당 값 타입만 사용하는 의미있는 메소드를 만들 수 있다.
        return true;
    }
}
