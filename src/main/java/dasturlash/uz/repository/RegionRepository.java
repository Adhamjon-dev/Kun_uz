package dasturlash.uz.repository;

import dasturlash.uz.entitiy.RegionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegionRepository extends CrudRepository<RegionEntity, Integer> {

    boolean existsByRegionKey(String key);

    Optional<RegionEntity> findByRegionKey(String key);
}
