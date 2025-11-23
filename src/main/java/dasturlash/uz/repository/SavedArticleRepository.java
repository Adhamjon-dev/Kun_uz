package dasturlash.uz.repository;

import dasturlash.uz.entitiy.SavedArticleEntity;
import dasturlash.uz.mapper.SavedArticleMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity, Integer> {
    Optional<SavedArticleEntity> getByArticleIdAndProfileId(String articleId, Integer profileId);

    @Transactional
    @Modifying
    @Query("delete from SavedArticleEntity where articleId = ?1 and profileId = ?2")
    int deleteByArticleIdAndProfileId(String articleId, Integer profileId);

    @Query("select s.id as id, a.id as articleId, a.title as articleTitle, a.description as articleDescription, " +
            "a.imageId as articleImageId, s.createdDate as savedDate " +
            "from SavedArticleEntity s " +
            "inner join ArticleEntity a on a.id = s.articleId " +
            "where a.visible = true ")
    List<SavedArticleMapper>  getSavedArticlesByProfileId(Integer profileId);
}
