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
			Team team = new Team();
			team.setName("teamA");
			em.persist(team);

			Team teamB = new Team();
			teamB.setName("teamB");
			em.persist(teamB);

			Member member1 = new Member();
			member1.setUsername("member");
			member1.setTeam(team);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("member2");
			member2.setTeam(teamB);
			em.persist(member2);


			em.flush();
			em.clear();

			/*Member m = em.find(Member.class, member1.getId());
			System.out.println("m = "+ m.getTeam().getClass()); //팀은 프록시로 가져온다.

			System.out.println("=====================");
			m.getTeam().getName();*/

			List<Member> members = em.createQuery("select m from Member m", Member.class) //EAGER로 세팅했는데도 커리가 두번 나감. JPQL을 SQL 그대로 날리기 때문에 member를 가지고 옴. 그 후 즉시로딩인 걸 알아서 바로 team을 가져오는 쿼리도 날린다.
					.getResultList();
			//SQL: select * from Member
			//SQL: select * from Team where TEAM_ID = xxx


			tx.commit(); //트랜잭션 끝 -> 저장(커밋)
		} catch (Exception e) {
			tx.rollback();
			//e.printStackTrace();
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
