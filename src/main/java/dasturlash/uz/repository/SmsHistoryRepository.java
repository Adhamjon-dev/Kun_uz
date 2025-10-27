package dasturlash.uz.repository;

import dasturlash.uz.entitiy.SmsHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity, String> {
    Optional<SmsHistoryEntity> findTopByPhoneNumberOrderByCreatedDateDesc(String phoneNumber);
}
