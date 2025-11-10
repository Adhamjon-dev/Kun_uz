package dasturlash.uz.repository;

import dasturlash.uz.entitiy.article.ArticleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, String> {
    Optional<ArticleEntity> findByIdAndVisibleTrue(String id);

    @Transactional
    @Modifying
    @Query("update ArticleEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);
}
