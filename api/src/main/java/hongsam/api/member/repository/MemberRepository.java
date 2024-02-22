package hongsam.api.member.repository;

import hongsam.api.member.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    // 회원가입시 중복 이메일 체크용
    public List<Member> findMemberByEmail(String email) {

        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    public Member findPasswordByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    // 이메일로 회원 1명 찾기
    public Member findMemberByEmailOne(String email) {

        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public Member findMemberByUUID(String uuid) {

        return em.createQuery("select m from Member m where m.uuid = :uuid", Member.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
    }

}
