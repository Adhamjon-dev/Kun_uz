package dasturlash.uz.repository;

import dasturlash.uz.entitiy.article.ArticleSectionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleSectionRepository extends CrudRepository<ArticleSectionEntity, String> {
    @Query("select sectionId from ArticleSectionEntity where articleId =?1")
    List<Integer> getSectionIdListByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query("Delete from ArticleSectionEntity where articleId =?1 and sectionId =?2")
    void deleteByArticleIdAndSectionId(String articleId, Integer sectionId);
}
