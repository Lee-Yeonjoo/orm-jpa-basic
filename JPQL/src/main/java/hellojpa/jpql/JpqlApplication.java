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

			Member member1 = new Member();
			member1.setUsername("관리자1");
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("관리자2");
			em.persist(member2);

			em.flush();
			em.clear();

			String concat = "select concat('a', 'b') from Member m"; //'a' || 'b'로도 가능. -> un-inject language해야함.
			String substring = "select substring(m.username, 2, 3) from Member m"; //두번째부터 3개를 자르기
			String locate = "select locate('de', 'abcdefg') from Member m"; //abcdefg에서 de의 위치를 반환. Integer로 반환.
			String size = "select size(t.members) from Team t";  //t.members 컬렉션의 크기를 알려준다.
			String index = "select index(t.members) from Team t";

			List<Integer> result = em.createQuery(size, Integer.class)
					.getResultList();

			for (Integer s: result) {
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
