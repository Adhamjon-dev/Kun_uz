package dasturlash.uz.repository;

import dasturlash.uz.entitiy.article.ArticleTagEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTagRepository extends CrudRepository<ArticleTagEntity, Integer> {
    @Query("select tagId from ArticleTagEntity where articleId =?1")
    List<Integer> getTagIdListByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query("Delete from ArticleTagEntity where articleId =?1 and tagId =?2")
    void deleteByArticleIdAndTagId(String articleId, Integer tagId);
}
