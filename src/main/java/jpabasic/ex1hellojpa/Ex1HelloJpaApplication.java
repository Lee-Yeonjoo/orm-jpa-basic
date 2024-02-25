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
			/* 회원 등록
			Member member = new Member();
			member.setId(2L);
			member.setName("HelloB");
			em.persist(member);*/

			//회원 단건 조회. 조회의 경우는 트랜잭션이 아니어도 가능.
			Member findMember = em.find(Member.class, 1L);
			System.out.println("findMember = "+ findMember.getName());

			//회원 수정
			findMember.setName("HelloJPA");
			//em.persist(findMember);로 또 저장하지 않아도 된다! 트랜잭션을 커밋하기 전에 JPA가 알아서 바뀐 내용에 대해 업데이트 쿼리를 날린다.

			//회원 삭제
			//em.remove(findMember);

			//JPQL
			//전체 회원 조회
			List<Member> result = em.createQuery("select m from Member as m", Member.class)
					//.setFirstResult(5)
					//.setMaxResults(8) //페이지네이션을 쉽게 할 수 있다. id5~8만 조회
					.getResultList(); //JPQL은 테이블 대상이 아닌 객체를 대상으로 쿼리를 작성해야한다. 실제 쿼리는 필드마다 다 select하는데 JPQL은 그냥 엔티티만 select함.

			for (Member member: result) {
				System.out.println("member.name = "+member.getName());
			}
			//JPQL은 엔티티 객체를 대상으로, SQL은 데이터베이스 테이블을 대상으로 쿼리.

			tx.commit(); //트랜잭션 끝 -> 저장(커밋)
		} catch (Exception e) {
			tx.rollback();
		}finally {
			em.close(); //em과 emf를 다 사용 후엔 닫아준다.
			emf.close(); //웹 어플리케이션의 경우 WAS가 내려갈때 닫아줘야 해서 try문으로 꼭 닫아줘야한다.
		}
	}
}
