package dasturlash.uz.repository;

import dasturlash.uz.entitiy.CommentEntity;
import dasturlash.uz.mapper.CommentMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity,Integer>, PagingAndSortingRepository<CommentEntity, Integer> {
    Optional<CommentEntity> findByIdAndVisibleTrue(Integer id);

    @Query("""
        select c.id as id,
               c.createdDate as createdDate,
               c.updateDate as updateDate,
               c.content as content,
               c.profileId as profileId,
               p.name as profileName,
               p.photoId as profileImageId,
               (
                    select count(cl)
                    from CommentLikeEntity cl
                    where cl.commentId = c.id and cl.emotion = dasturlash.uz.enums.EmotionEnum.LIKE
               ) as likeCount,
               (
                    select count(cl)
                    from CommentLikeEntity cl
                    where cl.commentId = c.id and cl.emotion = dasturlash.uz.enums.EmotionEnum.DISLIKE
               ) as dislikeCount
        from CommentEntity c
        join ProfileEntity p on p.id = c.profileId
        where c.articleId = :articleId and c.visible = true
        order by c.createdDate desc
        """)
    List<CommentMapper> getByArticleId(@Param("articleId") String articleId);

    @Query("select c.id as id, c.createdDate as createdDate, c.updateDate as updateDate,c.content as content, " +
            "c.profileId as  profileId, p.name as profileName, p.photoId as profileImageId, " +
            "c.articleId as articleId " +
            "from CommentEntity c inner join ProfileEntity p on p.id = c.profileId " +
            "where c.articleId = ?1 and c.profileId = ?2 and c.visible = true")
    List<CommentMapper> getByArticleIdAndProfileId(String articleId, Integer profileId);

    @Query("select c.id as id, c.createdDate as createdDate, c.updateDate as updateDate,c.content as content, " +
            "c.profileId as  profileId, p.name as profileName, p.surname as profileSurname, " +
            "c.articleId as articleId, a.title as articleTitle, c.replyId as replyId " +
            "from CommentEntity c inner join ProfileEntity p on p.id = c.profileId " +
            "inner join ArticleEntity a on a.id = c.articleId " +
            "where c.visible = true")
    PageImpl<CommentMapper> pagination(Pageable pageable);
}