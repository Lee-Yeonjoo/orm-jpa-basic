package jpabasic.ex1hellojpa;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {
    private String city;
    private String street;
    //@Column(name = "ZIPCODE") //이거도 가능.
    private String zipcode;

    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
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

   //setter를 다 지워서 불변객체로 만든다. or setter를 private으로 만들어도됨


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Address address = (Address) object;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {  //equals 만들면 hashCode도 만들어야 해시맵같은 자바 컬렉션에서 효율적으로 사용 가능.
        return Objects.hash(city, street, zipcode);
    }
}
