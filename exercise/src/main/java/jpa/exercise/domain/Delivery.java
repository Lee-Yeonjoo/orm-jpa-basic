package jpa.exercise.domain;

import jakarta.persistence.*;

@Entity
public class Delivery extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY) //지연로딩 설정, 여기엔 영속성 전이 ALL로 설정하는게 x. delivery를 order에 맞추는 거기 때문.
    private Order order;

    private String city;
    private String street;
    private String zipcode;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
