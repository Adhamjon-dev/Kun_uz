package dasturlash.uz.repository;

import dasturlash.uz.entitiy.article.ArticleEntity;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.mapper.ArticleFullMapper;
import dasturlash.uz.mapper.ArticleShortInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, String>, PagingAndSortingRepository<ArticleEntity, String> {
    Optional<ArticleEntity> findByIdAndVisibleTrue(String id);

    Optional<ArticleEntity> findByIdAndVisibleTrueAndStatus(String id, ArticleStatus status);

    @Transactional
    @Modifying
    @Query("update ArticleEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    @Transactional
    @Modifying
    @Query("Update ArticleEntity set status = ?2 where id =?1")
    int changeStatus(String articleId, ArticleStatus status);

    @Query(" select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            " from  ArticleEntity a " +
            " inner join ArticleSectionEntity a_s on a_s.articleId = a.id " +
            " where a_s.sectionId = ?1 and a.visible = true and a.status = 'PUBLISHED' " +
            " order by a.createdDate desc  limit ?2")
    List<ArticleShortInfo> getBySectionId(Integer sectionId, int limit);
    // select a.id as id, a.title as title, a.description as description,
    // a.imageId as imageId, a.publishedDate as publishedDate
    // (
    //     select c.name from  article_category a_c
    //     inner join category c on c.id = a_c.category_id limit 1
    // )
    // from article a
    // inner join article_section  a_s  on a_s.article_id = a.id
    // where a_s.section_id = ?
    // and visible = true and status = 'PUBLISHED'
    // limit ?
    // order by created_date desc

    @Query(" select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            " from  ArticleEntity a " +
            " where a.visible = true and a.id not in ?1 " +
            " order by a.createdDate desc  limit ?2")
    List<ArticleShortInfo> getPublishedArticleListExceptIds(List<String> exceptIdList, int limit);


    @Query(" select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            " from  ArticleEntity a " +
            " inner join ArticleCategoryEntity ac on ac.articleId = a.id " +
            " where ac.categoryId = ?1 and a.visible = true " +
            " order by a.createdDate desc limit ?2")
    List<ArticleShortInfo> getByCategoryId(Integer categoryId, int limit);

    @Query(" select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            " from  ArticleEntity a " +
            " where a.regionId = ?1 and a.visible = true " +
            " order by a.createdDate desc limit ?2")
    List<ArticleShortInfo> getByRegionId(Integer regionId, int limit);

    @Query(" select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            " from  ArticleEntity a " +
            " inner join ArticleTagEntity at on a.id = at.articleId " +
            " inner join TagEntity t on at.tagId = t.id " +
            " where t.name = ?1 " +
            " and a.visible = true " +
            " order by a.createdDate desc limit ?2")
    List<ArticleShortInfo> getByTagName(String tagName, int limit);

    @Query(" select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            " from  ArticleEntity a " +
            " inner join ArticleSectionEntity a_s on a_s.articleId = a.id " +
            " where a_s.sectionId = ?2 and a.visible = true " +
            " and a_s.articleId <> ?1" +
            " order by a.createdDate desc  limit 4")
    List<ArticleShortInfo> getBySectionIdAndExceptId(String exceptId, Integer sectionId);

    @Query(" select a.id as id, a.title as title, a.description as description, a.imageId as imageId, a.publishedDate as publishedDate " +
            " from  ArticleEntity a " +
            " inner join ArticleSectionEntity a_s on a_s.articleId = a.id " +
            " where a.visible = true " +
            " and a_s.articleId <> ?1" +
            " order by a.viewCount desc  limit 4")
    List<ArticleShortInfo> getViewTop4ArticleByExceptId(String exceptId);

    @Query(value = """
    select
        a.id as id,
        a.title as title,
        a.description as description,
        a.content as content,
        a.shared_count as sharedCount,
        a.view_count as viewCount,
        a.read_time as readTime,
        CASE :lang
            WHEN 'UZ' THEN r.name_uz
            WHEN 'RU' THEN r.name_ru
            WHEN 'EN' THEN r.name_en
        END as regionName,
        r.region_key as regionKey,
        p.id as moderatorId,
        p.name as moderatorName,
        (
            select json_agg(
                       json_build_object(
                           'id', s.id,
                           'name',
                                CASE :lang
                                    WHEN 'UZ' THEN s.name_uz
                                    WHEN 'RU' THEN s.name_ru
                                    WHEN 'EN' THEN s.name_en
                                END
                       )
                   )::text
            from article_section a_s
            join section s on s.id = a_s.section_id
            where a_s.article_id = a.id
        ) as sections,
        (
            select json_agg(
                       json_build_object(
                           'categoryKey', c.id,
                           'name',
                                CASE :lang
                                    WHEN 'UZ' THEN c.name_uz
                                    WHEN 'RU' THEN c.name_ru
                                    WHEN 'EN' THEN c.name_en
                                END
                       )
                   )::text
            from article_category a_c
            join category c on c.id = a_c.category_id
            where a_c.article_id = a.id
        ) as categories,
        (
            select json_agg(
                       json_build_object(
                           'name', t.name
                       )
                   )::text
            from article_tag a_t
            join tags t on t.id = a_t.tag_id
            where a_t.article_id = a.id
        ) as tags,
        (
            select count(a_l) from article_like a_l
            where a_l.article_id = a.id and a_l.emotion = 'LIKE'
        ) as likeCount,
        (
            select count(a_l) from article_like a_l
            where a_l.article_id = a.id and a_l.emotion = 'DISLIKE'
        ) as dislikeCount
    from article a
    join region r on r.id = a.region_id
    join profile p on p.id = a.moderator_id
    where a.id = :articleId and a.visible = true
    """, nativeQuery = true)
    ArticleFullMapper getByArticleIdAndLang(@Param("articleId") String articleId, @Param("lang") String lang);
}
