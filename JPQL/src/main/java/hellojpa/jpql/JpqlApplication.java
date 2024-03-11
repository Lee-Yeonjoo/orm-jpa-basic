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

			em.flush();
			em.clear();


			/*String query = "select m from Member m where m = :member"; //엔티티를 넘기면 sql에선 식별자로 바뀐다.
			Member findMember = em.createQuery(query , Member.class)
					.setParameter("member", member1)
					.getSingleResult();*/

			/*String query = "select m from Member m where m.id = :memberId"; //식별자를 넘겨도 동일한 sql
			Member findMember = em.createQuery(query , Member.class)
					.setParameter("memberId", member1.getId())
					.getSingleResult();*/

			//System.out.println("findMember = "+ findMember);

			String query = "select m from Member m where m.team = :team"; //외래키 값을 엔티티로 넘겨도 sql은 식별자로 나간다.
			List<Member> members = em.createQuery(query , Member.class)
					.setParameter("team", teamA)
					.getResultList();

			for (Member member : members) {
				System.out.println("member = "+ member);
			}


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
