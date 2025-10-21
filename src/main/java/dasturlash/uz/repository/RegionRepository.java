package dasturlash.uz.repository;

import dasturlash.uz.entitiy.RegionEntity;
import dasturlash.uz.mapper.LanguageMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends CrudRepository<RegionEntity, Integer> {

    boolean existsByRegionKey(String key);

    Optional<RegionEntity> findByRegionKey(String key);

    @Query("select r.id as id, r.regionKey as key, r.nameEn as name from RegionEntity r where r.visible = true")
    List<LanguageMapper> getEnLanguage();

    @Query("select r.id as id, r.regionKey as key, r.nameUz as name from RegionEntity r where r.visible = true")
    List<LanguageMapper> getUzLanguage();

    @Query("select r.id as id, r.regionKey as key, r.nameRu as name from RegionEntity r where r.visible = true")
    List<LanguageMapper> getRuLanguage();

    boolean existsByRegionKeyAndIdNot(String key, Integer id);
}
