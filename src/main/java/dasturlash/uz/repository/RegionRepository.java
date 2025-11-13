package dasturlash.uz.repository;

import dasturlash.uz.entitiy.RegionEntity;
import dasturlash.uz.mapper.LanguageMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends CrudRepository<RegionEntity, Integer> {

    boolean existsByRegionKey(String key);

    Optional<RegionEntity> findByRegionKey(String key);

    @Transactional
    @Modifying
    @Query("update RegionEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    @Query("SELECT r.id AS id, " +
            "CASE :lang " +
            "   WHEN 'UZ' THEN r.nameUz " +
            "   WHEN 'RU' THEN r.nameRu " +
            "   WHEN 'EN' THEN r.nameEn " +
            "END AS name, " +
            "r.orderNumber AS orderNumber, " +
            "r.regionKey AS key " +
            "FROM RegionEntity r " +
            "WHERE r.visible = true order by orderNumber asc")
    List<LanguageMapper> getByLang(@Param("lang") String lang);

    boolean existsByRegionKeyAndIdNot(String key, Integer id);

    Optional<RegionEntity> findByIdAndVisibleTrue(Integer id);

    @Query("from RegionEntity order by orderNumber")
    Iterable<RegionEntity> findAllOrder();

    @Query("SELECT c.id AS id, " +
            "CASE :lang " +
            "   WHEN 'UZ' THEN c.nameUz " +
            "   WHEN 'RU' THEN c.nameRu " +
            "   WHEN 'EN' THEN c.nameEn " +
            "END AS name, " +
            "c.orderNumber AS orderNumber, " +
            "c.regionKey AS regionKey " +
            "FROM RegionEntity c " +
            "WHERE c.visible = true and id = :id")
    LanguageMapper getByIdAndLang(@Param("id") Integer id, @Param("lang") String lang);
}