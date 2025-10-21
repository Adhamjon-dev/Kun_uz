package dasturlash.uz.repository;

import dasturlash.uz.entitiy.CategoryEntity;
import dasturlash.uz.mapper.LanguageMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {

    Boolean existsByCategoryKey(String regionKey);

    Boolean existsByCategoryKeyAndIdNot(String regionKey, Integer id);

    @Query("select c.id as id, c.categoryKey as key, c.nameEn as name from CategoryEntity c where c.visible = true")
    List<LanguageMapper> getEnLanguage();

    @Query("select c.id as id, c.categoryKey as key, c.nameUz as name from CategoryEntity c where c.visible = true")
    List<LanguageMapper> getUzLanguage();

    @Query("select c.id as id, c.categoryKey as key, c.nameRu as name from CategoryEntity c where c.visible = true")
    List<LanguageMapper> getRuLanguage();

    Optional<CategoryEntity> findByIdAndVisibleTrue(Integer id);
}
