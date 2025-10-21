package dasturlash.uz.repository;

import dasturlash.uz.entitiy.SectionEntity;
import dasturlash.uz.mapper.LanguageMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends CrudRepository<SectionEntity, Integer>, PagingAndSortingRepository<SectionEntity, Integer> {
    Boolean existsBySectionKey(String sectionKey);

    Optional<SectionEntity> findByIdAndVisibleTrue(Integer id);

    Boolean existsBySectionKeyAndIdNot(String sectionKey,Integer id);

    @Query("select s.id as id, s.sectionKey as key, s.nameEn as name from SectionEntity s where s.visible = true")
    List<LanguageMapper> getEnLanguage();

    @Query("select s.id as id, s.sectionKey as key, s.nameUz as name from SectionEntity s where s.visible = true")
    List<LanguageMapper> getUzLanguage();

    @Query("select s.id as id, s.sectionKey as key, s.nameRu as name from SectionEntity s where s.visible = true")
    List<LanguageMapper> getRuLanguage();
}
