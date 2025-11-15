package dasturlash.uz.repository;

import dasturlash.uz.dto.CustomFilterResultDTO;
import dasturlash.uz.dto.article.ArticleFilterDTO;
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
public class CustomArticleRepository {
    @Autowired
    private EntityManager entityManager;

    public CustomFilterResultDTO<Object[]> filter(ArticleFilterDTO filter, int page, int size) {
        StringBuilder conditionBuilder = new StringBuilder("where a.visible = true and a.status = 'PUBLISHED' ");
        StringBuilder selectBuilder = new StringBuilder(
                "select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
                        "from  ArticleEntity a ");

        StringBuilder countBuilder = new StringBuilder(
                "select count(a) from  ArticleEntity a ");

        Map<String, Object> params = new HashMap<>();
        if (filter.getTitle() != null) {
            conditionBuilder.append("and a.title like :title ");
            params.put("title", "%" + filter.getTitle() + "%");
        }
        if (filter.getRegionId() != null) {
            conditionBuilder.append("and a.regionId = :regionId ");
            params.put("regionId", filter.getRegionId());
        }
        if (filter.getSectionId() != null) {
            selectBuilder.append("inner join ArticleSectionEntity a_s on a_s.articleId = a.id ");
            countBuilder.append("inner join ArticleSectionEntity a_s on a_s.articleId = a.id ");
            conditionBuilder.append("and a_s.sectionId = :sectionId ");
            params.put("sectionId", filter.getSectionId());
        }
        if (filter.getCategoryId() != null) {
            selectBuilder.append("inner join ArticleCategoryEntity a_c on a_c.articleId = a.id ");
            countBuilder.append("inner join ArticleCategoryEntity a_c on a_c.articleId = a.id ");
            conditionBuilder.append("and a_c.categoryId = :categoryId ");
            params.put("categoryId", filter.getCategoryId());
        }
        if (filter.getPublishedDateFrom() != null) {
            LocalDateTime dateFrom = LocalDateTime.of(filter.getPublishedDateFrom(), LocalTime.MIN);
            conditionBuilder.append("and a.publishedDate >= :publishedDateFrom ");
            params.put("publishedDateFrom", dateFrom);
        }
        if (filter.getPublishedDateTo() != null) {
            LocalDateTime dateTo = LocalDateTime.of(filter.getPublishedDateTo(), LocalTime.MAX);
            conditionBuilder.append("and a.publishedDate <= :publishedDateTo ");
            params.put("publishedDateTo", dateTo);
        }

        selectBuilder.append(conditionBuilder);

        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult(page * size); // offset
        selectQuery.setMaxResults(size); // limit

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
