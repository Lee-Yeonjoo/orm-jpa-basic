package jpabasic.ex1hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class Ex1HelloJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ex1HelloJpaApplication.class, args);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); //애플리케이션 로딩 시점에 딱 한개만 만든다. DB당 한 개씩

		EntityManager em = emf.createEntityManager(); //엔티티 매니저는 트랜잭션마다 만들어야한다. 고객이 요청할 때마다 만들어야함. 쓰레드 간에 공유금지. 사용하고 버려야함.

		EntityTransaction tx = em.getTransaction(); //트랜잭션을 얻는다. 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
		tx.begin(); //트랜잭션 시작

		try {

			Member member = new Member();
			member.setUsername("member1");
			member.setHomeAddress(new Address("homeCity", "street", "10000"));

			member.getFavoriteFood().add("치킨");
			member.getFavoriteFood().add("족발");
			member.getFavoriteFood().add("피자");

			/*member.getAddressHistory().add(new Address("old1", "street", "10000"));
			member.getAddressHistory().add(new Address("old2", "street", "10000"));
*/
			//엔티티로 저장
			member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
			member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

			em.persist(member);  //값 타입 컬렉션들이 따로 persist하지 않아도 같이 저장됨. 값 타입은 자신의 라이프 사이클이 없고, member에 의존.

			em.flush();
			em.clear();

			System.out.println("==========================");
			Member findMember = em.find(Member.class, member.getId()); //멤버만 조회한다. 컬렉션은 지연로딩!

			/*List<Address> addressesHistory = findMember.getAddressHistory();
			for (Address address : addressesHistory) {
				System.out.println("address = " + address.getCity());
			}

			Set<String> favoriteFoods = findMember.getFavoriteFood();
			for (String fovriteFood : favoriteFoods) {
				System.out.println("favoriteFood = "+favoriteFoods);
			}  //지연로딩이다.*/

			//값 타입 수정
			//findMember.getHomeAddress().setCity("newCity"); //수정할 때 값 타입은 그냥 새 객체를 저장해야함. setter쓰면 안된다.
			Address a = findMember.getHomeAddress();
			findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));  //바꾼거로 새로 만들어서 갈아끼운다.

			//값 타입 컬렉션 수정. 치킨 -> 한식
			findMember.getFavoriteFood().remove("치킨");
			findMember.getFavoriteFood().add("한식"); //지우고 새로 저장해야한다. String을 변경할 수 없으니까
			//컬렉션만 바꿔준건데 db에도 반영됨. 영속성 전이.

			//주소 바꾸기
			//findMember.getAddressHistory().remove(new Address("old1", "street", "10000")); //지울 대상을 찾을 때 equals로 찾는다. 그래서 equals의 hashCode를 제대로 넣어야한다! 이때 member_id와 관련된 것을 다 지운다.
			//findMember.getAddressHistory().add(new Address("newCity", "street", "10000")); //다 지운 후, 남은 거만 insert한다.


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
