package dasturlash.uz.repository;

import dasturlash.uz.entitiy.ArticleLikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity, String> {
    Optional<ArticleLikeEntity> getByArticleIdAndProfileId(String articleId, Integer profileId);

    @Transactional
    @Modifying
    @Query("delete from ArticleLikeEntity where articleId = ?1 and profileId = ?2")
    int deleteByArticleIdAndProfileId(String articleId, Integer profileId);
}