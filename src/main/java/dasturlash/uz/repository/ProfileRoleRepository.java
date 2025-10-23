package dasturlash.uz.repository;

import dasturlash.uz.entitiy.ProfileRoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, Integer> {

}
