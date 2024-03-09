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

			Member member = new Member();
			member.setUsername("member1");
			member.setAge(10);
			em.persist(member);

			em.flush();
			em.clear();

			/*List<Member> result = em.createQuery("select m from Member m", Member.class)
							.getResultList(); //쿼리로 반환된 엔티티는 영속성 컨텍스트에 의해 관리된다.

			Member findMember = result.get(0);
			findMember.setAge(20); //영속성 컨텍스트에 의해 관리되니까 setter로 바꾼게 db에 알아서 반영됨.
*/
			List<Team> teams = em.createQuery("select t from Member m join m.team t", Team.class)
					.getResultList(); //join을 할 때 jpql은 "select m.team from Member m"도 가능하지만 쿼리 튜닝을 위해 jpql에서도 예측이 되도록 join을 써야한다.

			em.createQuery("select o.address from Order o", Address.class)  //임베디드 타입 프로젝션. 어디 엔티티 소속인지 알아야함
					.getResultList();

			em.createQuery("select distinct m.username, m.age from Member m")
					.getResultList();

			/*List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
							.getResultList();  //제네릭으로 오브젝트 배열을 지정.

			Object[] o = resultList.get(0);
			System.out.println("username = " + o[0]);
			System.out.println("age = "+ o[1]);*/

			List<MemberDTO> result = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class) //dto 생성자 생성하듯이 new를 통해 작성. -> 쿼리dsl 쓰면 import를 통해 패키지 경로 생략 가능
					.getResultList();

			MemberDTO memberDTO = result.get(0);
			System.out.println(memberDTO.getUsername());
			System.out.println(memberDTO.getAge());

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
