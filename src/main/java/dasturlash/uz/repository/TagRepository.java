package dasturlash.uz.repository;

import dasturlash.uz.entitiy.TagEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<TagEntity, Integer> {
}
