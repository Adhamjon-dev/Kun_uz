package dasturlash.uz.repository;

import dasturlash.uz.entitiy.CategoryEntity;
import dasturlash.uz.mapper.LanguageMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

    Boolean existsByCategoryKey(String regionKey);

    Boolean existsByCategoryKeyAndIdNot(String regionKey, Integer id);

    @Transactional
    @Modifying
    @Query("update CategoryEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    @Query("SELECT c.id AS id, " +
            "CASE :lang " +
            "   WHEN 'UZ' THEN c.nameUz " +
            "   WHEN 'RU' THEN c.nameRu " +
            "   WHEN 'EN' THEN c.nameEn " +
            "END AS name, " +
            "c.orderNumber AS orderNumber, " +
            "c.categoryKey AS key " +
            "FROM CategoryEntity c " +
            "WHERE c.visible = true order by orderNumber asc")
    List<LanguageMapper> getByLang(@Param("lang") String lang);

    Optional<CategoryEntity> findByIdAndVisibleTrue(Integer id);

    @Query("from CategoryEntity order by orderNumber")
    Iterable<CategoryEntity> findAllOrder();
}
