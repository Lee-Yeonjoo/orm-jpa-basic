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

			/*Member member = em.find(Member.class, 1L);
			//printMemberAndTeam(member); //멤버와 팀을 같이 출력하는 메서드니까 연관관계인 team까지 조회하는게 좋음
			printMember(member); //멤버만 출력하는 메서드니까 연관관계인 team은 안가져오는게 이득.*/

			/*Member member = new Member();
			member.setUsername("hello");

			em.persist(member);

			em.flush();
			em.clear();

			//Member findMember = em.find(Member.class, member.getId());
			Member findMember = em.getReference(Member.class, member.getId());
			System.out.println("before findMember = " + findMember.getClass()); //findMember가 가짜 객체(프록시)임을 알 수 있다.
			System.out.println("findMember.id = " + findMember.getId()); //id값은 이미 아니까 쿼리 안날린거임
			System.out.println("findMember.username = " + findMember.getUsername()); //username은 db에서 가져와야되니까 그제서야 쿼리를 날린다.
			System.out.println("findMember.username = " + findMember.getUsername()); //두번째 호출할 땐 이미 프록시의 target에 값이 존재하니까 초기화없이 바로 출력가능.
			System.out.println("after findMember = " + findMember.getClass()); //초기화 후 진짜 객체로 교체되는 게 아니다!*/

			/*Member member1 = new Member();
			member1.setUsername("member1");
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("member2");
			em.persist(member2);

			em.flush();
			em.clear();

			*//*Member m1 = em.find(Member.class, member1.getId());
			Member m2 = em.find(Member.class, member2.getId());

			System.out.println("m1 == m2 " + (m1.getClass() == m2.getClass())); //em.find로 진짜 객체를 가져왔으니까 true가 나옴.*//*
			Member m1 = em.find(Member.class, member1.getId());
			Member m2 = em.getReference(Member.class, member2.getId());

			System.out.println("m1 == m2 " + (m2 instanceof Member));*/ //em.getReference로 프록시를 가져왔으니까 false가 나옴.

			/*Member member = new Member();
			member.setUsername("member");
			em.persist(member);

			em.flush();
			em.clear();

			Member m = em.find(Member.class, member.getId());
			System.out.println("m = " + m.getClass());

			Member reference = em.getReference(Member.class, member.getId());
			System.out.println("r = " + reference.getClass()); //이미 영속성 컨텍스트에 존재하므로 진짜 객체를 반환.

			System.out.println("a == a:" + (m == reference)); //true를 보장해준다.
*/


			/*Member member = new Member();
			member.setUsername("member");
			em.persist(member);

			em.flush();
			em.clear();

			Member ref = em.getReference(Member.class, member.getId());
			System.out.println("ref = " + ref.getClass()); //프록시

			Member find = em.find(Member.class, member.getId());
			System.out.println("r = " + find.getClass()); //원본 객체여야하지만 true를 보장하기위해 프록시가 저장됨

			System.out.println("a == a:" + (ref == find)); //jpa는 어떻게든 true를 보장해준다.
*/

			Member member = new Member();
			member.setUsername("member");
			em.persist(member);

			em.flush();
			em.clear();

			Member ref = em.getReference(Member.class, member.getId());
			System.out.println("ref = " + ref.getClass()); //프록시

			//em.detach(ref);  //영속성 컨텍스트에서 분리
			//em.close();   //영속성 클로스해도 마찬가지
			//em.clear();  //마찬가지로 영속성 컨텍스트를 다 지웠기 때문에 예외 터짐

			ref.getUsername(); //예외가 터짐 -> 영속성이 더 이상 관리안됨.
			System.out.println("isLoaded=" + emf.getPersistenceUnitUtil().isLoaded(ref)); //프록시 초기화 여부를 알려줌.
			System.out.println(ref.getClass()); //프록시 클래스 확인 방법
			ref.getUsername(); //강제 초기화
			Hibernate.initialize(ref); //강제 초기화

			tx.commit(); //트랜잭션 끝 -> 저장(커밋)
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally {
			em.close(); //em과 emf를 다 사용 후엔 닫아준다.
		}
		emf.close(); //웹 어플리케이션의 경우 WAS가 내려갈때 닫아줘야 해서 try문으로 꼭 닫아줘야한다.
	}

	private static void printMember(Member member) {
		System.out.println("member = "+ member.getUsername());
	}

	private static void printMemberAndTeam(Member member) {
		String username = member.getUsername();
		System.out.println("username = " + username);

		Team team = member.getTeam();
		System.out.println("team = "+ team.getName());
	}
}
