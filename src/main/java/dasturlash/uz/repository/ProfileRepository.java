package dasturlash.uz.repository;

import dasturlash.uz.entitiy.ProfileEntity;
import dasturlash.uz.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);

    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer id);

    Optional<ProfileEntity> findByUsernameAndVisibleTrueAndIdNot(String username, Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status =?1 where username =?2")
    void setStatusByUsername(ProfileStatus status, String username);

    Optional<ProfileEntity> findByIdAndStatusAndVisibleTrue(Integer id, ProfileStatus status);
}
