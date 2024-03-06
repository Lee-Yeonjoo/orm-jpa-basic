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
}
