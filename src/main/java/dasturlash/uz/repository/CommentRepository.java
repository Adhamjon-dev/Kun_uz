package dasturlash.uz.repository;

import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.entitiy.CommentEntity;
import dasturlash.uz.mapper.CommentShortInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity,Integer> {
    Optional<CommentEntity> findByIdAndVisibleTrue(Integer id);

    @Query("select c.id as id, c.createdDate as createdDate, c.updateDate as updateDate,c.content as content, c.articleId articleId, c.profileId as  profileId " +
            "from CommentEntity c where c.articleId = ?1 and c.visible = true")
    List<CommentShortInfo> getByArticleId(String articleId);
}