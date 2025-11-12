package dasturlash.uz.repository;

import dasturlash.uz.entitiy.article.ArticleCategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCategoryRepository extends CrudRepository<ArticleCategoryEntity, String> {
    @Query("select categoryId from ArticleCategoryEntity where articleId =?1")
    List<Integer> getCategoryIdListByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query("Delete from ArticleCategoryEntity where articleId =?1 and categoryId =?2")
    void deleteByArticleIdAndCategoryId(String articleId, Integer categoryId);
}