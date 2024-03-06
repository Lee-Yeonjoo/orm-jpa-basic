package jpabasic.ex1hellojpa;


import jakarta.persistence.Column;

public class Address {
    private String city;
    private String street;
    @Column(name = "ZIPCODE") //이거도 가능.
    private String zipcode;

    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
