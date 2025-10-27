package dasturlash.uz.repository;

import dasturlash.uz.entitiy.SmsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity, String>, PagingAndSortingRepository<SmsHistoryEntity, String> {
    Optional<SmsHistoryEntity> findTopByPhoneNumberOrderByCreatedDateDesc(String phoneNumber);

    List<SmsHistoryEntity> findByPhoneNumber(String  phoneNumber);

    @Query("from SmsHistoryEntity where createdDate between ?1 and ?2")
    List<SmsHistoryEntity> findByDate(LocalDateTime start, LocalDateTime end);
}
