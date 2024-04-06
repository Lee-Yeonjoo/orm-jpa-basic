package jpa.exercise.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor //기본 생성자가 있어야 함
public class Address { //값타입 - 임베디드 타입, setter는 만들지 않거나 private으로

    @Column(length = 10) //길이가 사용하는 엔티티에 공통으로 적용돼서 유용
    private String city;
    @Column(length = 20)

    private String street;
    @Column(length = 5)

    private String zipcode;

    private String fullAddress() { //이런 의미있는 메서드를 만들 수 있다.
        return getCity() + " " + getStreet() + " "+ getZipcode();
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    @Override
    public boolean equals(Object object) { //getter로 만드는 거에 체크(프록시 때문) -> getter가 만들어져 있어야함
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Address address = (Address) object;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
