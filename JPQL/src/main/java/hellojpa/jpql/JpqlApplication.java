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
			team.setName("팀A");
			em.persist(team);

			Team team2 = new Team();
			team2.setName("팀B");
			em.persist(team2);

			Member member = new Member();
			member.setUsername("회원A");
			member.setTeam(team);
			member.setAge(20);
			member.setType(MemberType.USER);
			em.persist(member);

			Member member2 = new Member();
			member2.setUsername("회원B");
			member2.setTeam(team);
			member2.setAge(20);
			member2.setType(MemberType.USER);
			em.persist(member2);

			em.flush();
			em.clear();


			String query = "select t from Team t join t.members";
			List<Team> teams = em.createQuery(query,Team.class)
							.getResultList();

			System.out.println("리스트 크기: "+teams.size());
			for (Team m : teams) {
				System.out.println(m);
				System.out.println(m.getMembers());
			}

			tx.commit(); //트랜잭션 끝 -> 저장(커밋)

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally {
			em.close(); //em과 emf를 다 사용 후엔 닫아준다.
		}
		emf.close(); //웹 어플리케이션의 경우 WAS가 내려갈때 닫아줘야 해서 try문으로 꼭 닫아줘야한다.
	}

}
