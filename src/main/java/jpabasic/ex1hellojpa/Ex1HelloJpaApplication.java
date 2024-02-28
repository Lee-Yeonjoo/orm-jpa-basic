package jpabasic.ex1hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

			/*섹션4
			Member member = new Member();
			member.setUsername("C");

			System.out.println("=================");
			em.persist(member); //이때 pk를 가져와야 영속성 컨텍스트 가능
			System.out.println("member.id = "+member.getId());
			System.out.println("=================");*/

			//섹션5
			//저장
			Team team = new Team();
			team.setName("TeamA");
			em.persist(team);

			Member member = new Member();
			member.setUsername("member1");
			//member.setTeamId(team.getId());
			member.setTeam(team);  //이러면 jpa가 알아서 팀의 pk값을 꺼내서 fk값으로 사용
			em.persist(member);

			//1차 캐시로 인해 안나가는 select 쿼리 확인하고 싶은 경우
			em.flush(); //현재 영속성 컨텍스트 저장소의 쿼리를 다 날림.
			em.clear(); //영속성 컨텍스트 초기화

			//조회
			Member findMember = em.find(Member.class, member.getId());

			//Long findTeamId = findMember.getTeamId(); //연관관계를 식별자로 저장한다면 조회할 때 바로 get 못하고 식별자로 찾는 과정이 생겨서 번거롭다.
			//Team findTeam = em.find(Team.class, findTeamId);
			Team findTeam = findMember.getTeam(); //객체지향스럽게 참조를 한다.

			System.out.println("findTeam = "+findTeam.getName());



			tx.commit(); //트랜잭션 끝 -> 저장(커밋)
		} catch (Exception e) {
			tx.rollback();
		}finally {
			em.close(); //em과 emf를 다 사용 후엔 닫아준다.
			emf.close(); //웹 어플리케이션의 경우 WAS가 내려갈때 닫아줘야 해서 try문으로 꼭 닫아줘야한다.
		}
	}
}
