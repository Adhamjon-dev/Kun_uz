package dasturlash.uz.repository;

import dasturlash.uz.dto.CustomFilterResultDTO;
import dasturlash.uz.dto.comment.CommentFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomCommentRepository {
    @Autowired
    private EntityManager entityManager;

    public CustomFilterResultDTO<Object[]> filter(CommentFilterDTO filter, int page, int size) {
        StringBuilder conditionBuilder = new StringBuilder("where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (filter.getId() != null) {
            conditionBuilder.append("and c.id = :id ");
            params.put("id", filter.getId());
        }
        if (filter.getProfileId() != null) {
            conditionBuilder.append("and c.profileId = :profileId ");
            params.put("profileId", filter.getProfileId());
        }
        if (filter.getArticleId() != null) {
            conditionBuilder.append("and c.articleId = :articleId ");
            params.put("articleId", filter.getArticleId());
        }
        if (filter.getCreatedDateFrom() != null) {
            LocalDateTime dateFrom = LocalDateTime.of(filter.getCreatedDateFrom(), LocalTime.MIN);
            conditionBuilder.append("and c.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom", dateFrom);
        }
        if (filter.getCreatedDateTo() != null) {
            LocalDateTime dateTo = LocalDateTime.of(filter.getCreatedDateTo(), LocalTime.MAX);
            conditionBuilder.append("and c.createdDate <= :createdDateTo ");
            params.put("createdDateTo", dateTo);
        }
        StringBuilder selectBuilder = new StringBuilder("Select c.id, c.createdDate, c.updateDate, c.content, p.id, p.name, " +
                "p.surname, a.id, a.title, c.replyId, c.visible, " +
                "(select count(cl) " +
                "from CommentLikeEntity cl " +
                "where cl.commentId = c.id and cl.emotion = 'LIKE' " +
                "), " +
                "(select count(cl) " +
                "from CommentLikeEntity cl " +
                "where cl.commentId = c.id and cl.emotion = 'DISLIKE' " +
                ") " +
                "From CommentEntity c " +
                "inner join ProfileEntity p on p.id =c.profileId " +
                "inner join ArticleEntity a on c.articleId = a.id ");
        selectBuilder.append(conditionBuilder);

        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);

        StringBuilder countBuilder = new StringBuilder("Select count(c) From CommentEntity c " +
                "inner join ProfileEntity p on p.id =c.profileId " +
                "inner join ArticleEntity a on c.articleId = a.id ");
        countBuilder.append(conditionBuilder);
        Query countQuery = entityManager.createQuery(countBuilder.toString());


        for (Map.Entry<String, Object> entry : params.entrySet()) {
            selectQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        List<Object[]> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();

        return new CustomFilterResultDTO<>(entityList, totalElements);
    }
}
