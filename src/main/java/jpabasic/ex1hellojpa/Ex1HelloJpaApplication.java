package jpabasic.ex1hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Ex1HelloJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ex1HelloJpaApplication.class, args);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); //애플리케이션 로딩 시점에 딱 한개만 만든다. DB당 한 개씩

		EntityManager em = emf.createEntityManager(); //엔티티 매니저는 트랜잭션마다 만들어야한다. 고객이 요청할 때마다 만들어야함. 쓰레드 간에 공유금지. 사용하고 버려야함.

		EntityTransaction tx = em.getTransaction(); //트랜잭션을 얻는다. 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
		tx.begin(); //트랜잭션 시작

		try {

			Address address = new Address("city", "street", "10000");

			Member member = new Member();
			member.setUsername("member1");
			member.setHomeAddress(address);
			em.persist(member);

			Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

			Member member2 = new Member();
			member2.setUsername("member2");
			member2.setHomeAddress(copyAddress); //member와 member2가 같은 address를 쓰고 있다. -> 복사해서 사용해야한다!
			em.persist(member2);

			//member.getHomeAddress().setCity("newCity"); //member의 city만 newCity로 바꾸고 싶은데 member2의 city도 바뀜 -> 이런 사이드이펙트에 의한 버그는 잡기 어렵다.
			//Address가 불변객체가 되어서 setter사용x -> 사이드 이펙트를막을 수 있다.
			//값을 바꾸고 싶을 땐 새로 만들어야 한다. or 카피 메서드를 만들어도됨.
			Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
			member.setHomeAddress(newAddress);

			tx.commit(); //트랜잭션 끝 -> 저장(커밋)
		} catch (Exception e) {
			tx.rollback();
			//e.printStackTrace();
		}finally {
			em.close(); //em과 emf를 다 사용 후엔 닫아준다.
		}
		emf.close(); //웹 어플리케이션의 경우 WAS가 내려갈때 닫아줘야 해서 try문으로 꼭 닫아줘야한다.
	}

}
