package dasturlash.uz.repository;

import dasturlash.uz.entitiy.CategoryEntity;
import dasturlash.uz.entitiy.SectionEntity;
import dasturlash.uz.mapper.LanguageMapper;
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
public interface SectionRepository extends CrudRepository<SectionEntity, Integer>, PagingAndSortingRepository<SectionEntity, Integer> {
    Boolean existsBySectionKey(String sectionKey);

    Optional<SectionEntity> findByIdAndVisibleTrue(Integer id);

    Boolean existsBySectionKeyAndIdNot(String sectionKey,Integer id);

    @Transactional
    @Modifying
    @Query("update SectionEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    @Query("SELECT s.id AS id, " +
            "CASE :lang " +
            "   WHEN 'UZ' THEN s.nameUz " +
            "   WHEN 'RU' THEN s.nameRu " +
            "   WHEN 'EN' THEN s.nameEn " +
            "END AS name, " +
            "s.orderNumber AS orderNumber, " +
            "s.sectionKey AS key " +
            "FROM SectionEntity s " +
            "WHERE s.visible = true order by orderNumber asc")
    List<LanguageMapper> getByLang(@Param("lang") String lang);

    @Query("from SectionEntity order by orderNumber")
    Iterable<SectionEntity> findAllOrder();
}
