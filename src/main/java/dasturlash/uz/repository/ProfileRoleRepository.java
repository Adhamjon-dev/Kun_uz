package dasturlash.uz.repository;

import dasturlash.uz.entitiy.ProfileRoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, Integer> {

    List<ProfileRoleEntity> findByProfileId(Integer profileId);
}
