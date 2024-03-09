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
			member.setUsername(null);
			member.setAge(10);
			member.setType(MemberType.ADMIN);
			member.setTeam(team);

			em.persist(member);

			em.flush();
			em.clear();

			String query = "select " +
								"case when m.age <= 10 then '학생요금' "+
								"     when m.age >= 60 then '경로요금' "+
								"     else '일반요금' "+
								"end "+
							"from Member m";

			String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m"; //null이면 '이름 없는 회원'으로 반환
			String query3 = "select nullif(m.username, '관리자') from Member m";  //관리자의 이름을 숨기고 싶어서.

			List<String> result = em.createQuery(query3, String.class)
					.getResultList();

			System.out.println("result.size = " + result.size());
			for (String s:
					result) {
				System.out.println("s = "+ s);
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
