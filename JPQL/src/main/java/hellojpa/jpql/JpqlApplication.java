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

			TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class); //반환타입이 Member로 타입이 확실할 때 -> TypedQuery
			TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
			Query query3 = em.createQuery("select m.username, m.age from Member m");  //반환타입이 string이랑 int 둘 다 있어서 확실하지 않음 -> Query

			List<Member> resultList = query1.getResultList();  //쿼리의 결과가 하나 이상인 경우
			for (Member member1 : resultList) {
				System.out.println("member1 = "+ member1);
			}

			/*TypedQuery<Member> query = em.createQuery("select m from Member m where  m.id = 10", Member.class);
			Member result = query.getSingleResult();
			System.out.println("result = "+result);*/

			Member m = em.createQuery("select m from Member m where username = :username", Member.class)
							.setParameter("username", "member1")
							.getSingleResult();  //Query로 반환하기보단 보통은 메서드들을 체인으로

			System.out.println("singleResult = "+ m.getUsername());


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
