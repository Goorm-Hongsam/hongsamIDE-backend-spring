package hongsam.api.question.repository;

import hongsam.api.question.domain.QuestionBasic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class QuestionRepository {

    private final EntityManager em;

    public QuestionBasic save(QuestionBasic questionBasic) {
        em.persist(questionBasic);
        return questionBasic;
    }

    public List<QuestionBasic> findAll() {
        String jpql = "select q from QuestionBasic q";

        List<QuestionBasic> resultList = em.createQuery(jpql, QuestionBasic.class)
                .getResultList();
        return resultList;
    }

    public List<QuestionBasic> findQuestion(String buttonType, int level, int id, int size) {
        String jpql = "select q from QuestionBasic q ";

        // 조건문 하나라도 들어가면 where 추가
        if (level != -1 || id != 1) {
            jpql += "where ";
        }

        // 문제 레벨 필터 안걸었을 때 -> 전체 레벨 조회
        if (level != -1) {
            jpql += "q.level = :level ";
        }

        // 첫 페이지가 아니라면
        if (id != 1) {
            if (level != -1) {
                jpql += "and ";
            }
            if (buttonType.equals("next")) {
                jpql += "q.id > :id ";
            } else if (buttonType.equals("previous")) {
                jpql += "q.id < :id ";
            }
        }

        // 문제 정렬
        if (buttonType.equals("next")) {
            jpql += "order by q.id asc";
        } else if (buttonType.equals("previous")) {
            jpql += "order by q.id desc";
        }

        TypedQuery<QuestionBasic> query = em.createQuery(jpql, QuestionBasic.class);
        if (level != -1) {
            query.setParameter("level", level);
        }
        if (id != 1) {
            query.setParameter("id", id);
        }
        query.setMaxResults(size);
        List<QuestionBasic> resultList = query.getResultList();
        resultList.sort(Comparator.comparingLong(QuestionBasic::getId));
        return resultList;
    }

}