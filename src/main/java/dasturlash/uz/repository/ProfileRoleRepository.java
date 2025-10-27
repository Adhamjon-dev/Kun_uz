package dasturlash.uz.repository;

import dasturlash.uz.entitiy.ProfileRoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, Integer> {

    List<ProfileRoleEntity> findByProfileId(Integer profileId);

    @Transactional
    @Modifying
    void deleteByProfileId(Integer profileId);
}
