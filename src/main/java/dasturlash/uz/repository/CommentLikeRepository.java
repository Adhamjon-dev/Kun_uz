package dasturlash.uz.repository;

import dasturlash.uz.entitiy.CommentLikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, String> {
    Optional<CommentLikeEntity> getByCommentIdAndProfileId(Integer commentId, Integer profileId);

    @Transactional
    @Modifying
    @Query("delete from CommentLikeEntity where commentId = ?1 and profileId = ?2")
    int deleteByCommentIdAndProfileId(Integer commentId, Integer profileId);
}