package hellojpa.jpql;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
			team.setName("teamA");
			em.persist(team);

			Member member = new Member();
			member.setUsername("member");
			member.setAge(10);
			member.setTeam(team);

			em.persist(member);

			em.flush();
			em.clear();

			String query = "select m from Member m inner join m.team t"; //내부 조인. 이제 t 사용가능.
			String query2 = "select m from Member m left outer join m.team t"; //외부 조인. outer 생략가능
			String query3 = "select m from Member m, Team t where m.username = t.username"; //세타 조인. 조인문 필요x
			String query4 = "select m from Member m left join m.team t on t.name = 'teamA'"; //on절 - 조인 대상 필터링
			String query5 = "select m from Member m left join Team t on m.username = t.name"; //on절 - 연관관계 없는 엔티티 외부조인
			List<Member> result = em.createQuery(query5, Member.class)
					.getResultList();

			System.out.println("result.size = " + result.size());
			for (Member m :
					result) {
				System.out.println("member = " + m);
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
