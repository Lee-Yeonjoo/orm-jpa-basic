package hellojpa.jpql;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class JpqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpqlApplication.class, args);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); //애플리케이션 로딩 시점에 딱 한개만 만든다. DB당 한 개씩

		EntityManager em = emf.createEntityManager(); //엔티티 매니저는 트랜잭션마다 만들어야한다. 고객이 요청할 때마다 만들어야함. 쓰레드 간에 공유금지. 사용하고 버려야함.

		EntityTransaction tx = em.getTransaction(); //트랜잭션을 얻는다. 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
		tx.begin(); //트랜잭션 시작

		try {
			Team teamA = new Team();
			teamA.setName("팀A");
			em.persist(teamA);

			Team teamB = new Team();
			teamB.setName("팀B");
			em.persist(teamB);

			Member member1 = new Member();
			member1.setUsername("회원1");
			member1.setTeam(teamA);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("회원2");
			member2.setTeam(teamA);
			em.persist(member2);

			Member member3 = new Member();
			member3.setUsername("회원3");
			member3.setTeam(teamB);
			em.persist(member3);

			//em.flush();
			//em.clear();

			//FLUSH 자동 호출 -> flush는 commit하거나 query가 나갈 때 자동 호출된다. 혹은 강제 호출 -> 영속성 컨텍스트 걱정 안해도 된다. 근데 벌크 연산 쿼리가 나간 후엔 영속성 컨텍스트 초기화 해줘야 한다!
			int resultCount = em.createQuery("update Member m set m.age = 20")
							.executeUpdate(); //벌크 연산. 쿼리가 한번만 나간다.
			em.clear(); //영속성 컨텍스트를 초기화해준 후, 다시 멤버를 조회해야 db의 내용이 반영된다!
			Member findMember = em.find(Member.class, member1.getId());
			System.out.println("findMember = "+ findMember.getAge());

			System.out.println("resultCount = "+ resultCount);
			System.out.println("member1.getAge() = "+ member1.getAge());  //db에만 반영되고, 영속성 컨텍스트엔 반영x
			System.out.println("member2.getAge() = "+ member2.getAge());  //flush 시점의 데이터가 아직 영속성 컨텍스트에 있다.
			System.out.println("member3.getAge() = "+ member3.getAge());


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
