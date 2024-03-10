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
			Team team = new Team();
			em.persist(team);

			Member member1 = new Member();
			member1.setUsername("관리자1");
			member1.setTeam(team);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("관리자2");
			member2.setTeam(team);
			em.persist(member2);

			em.flush();
			em.clear();

			/*String query = "select m.team from Member m"; //jpql은 객체 그래프 탐색이지만 실제 sql 쿼리는 묵시적 내부 조인이 발생한다. -> 묵시적 내부 조인이 안되도록 jpql 짜자
			List<Team> result = em.createQuery(query, Team.class)
					.getResultList();

			for (Team s: result) {
				System.out.println("s = "+ s);
			}*/

			String query = "select t.members from Team t"; //묵시적 내부 조인 발생. 그러나 탐색은 불가능. 컬렉션의 size정도만 탐색 가능.
			String query2 = "select m from Team t join t.members m";  //jpql에서도 명시적인 조인을 써야한다.
			List<Collection> result = em.createQuery(query2 , Collection.class)
							.getResultList();

			System.out.println("result = "+result);

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
