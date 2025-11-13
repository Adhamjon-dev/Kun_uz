package dasturlash.uz.repository;

import dasturlash.uz.entitiy.article.ArticleEntity;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.mapper.ArticleFullInfo;
import dasturlash.uz.mapper.ArticleShortInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, String>, PagingAndSortingRepository<ArticleEntity, String> {
    Optional<ArticleEntity> findByIdAndVisibleTrue(String id);

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
            " inner join ArticleSectionEntity a_s on a_s.articleId = a.id " +
            " where a_s.sectionId = ?2 and a.visible = true " +
            " and a_s.articleId <> ?1" +
            " order by a.createdDate desc  limit 4")
    List<ArticleShortInfo> getBySectionIdAndExceptId(String exceptId, Integer sectionId);

}
