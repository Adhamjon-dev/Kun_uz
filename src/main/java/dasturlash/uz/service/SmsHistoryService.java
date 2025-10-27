package dasturlash.uz.service;

import dasturlash.uz.entitiy.SmsHistoryEntity;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmsHistoryService {
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    public void save(String phone, String body, String code) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhoneNumber(phone);
        entity.setBody(body);
        entity.setCode(code);
        entity.setCreatedDate(LocalDateTime.now());
        smsHistoryRepository.save(entity);
    }

    public boolean isSmsSendToPhone(String phone, String code) {
        SmsHistoryEntity smsHistoryEntity = getSmsByPhone(phone);
        if (smsHistoryEntity.getCreatedDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (Duration.between(LocalDateTime.now(), smsHistoryEntity.getCreatedDate()).toMinutes() > 2) {
            return false;
        }
        if (!code.equals(smsHistoryEntity.getCode())) {
            smsHistoryEntity.setAttemptCount(smsHistoryEntity.getAttemptCount() + 1);
            smsHistoryRepository.save(smsHistoryEntity);
            return false;
        }
        return true;
    }

    public SmsHistoryEntity getSmsByPhone(String phoneNumber) {
        Optional<SmsHistoryEntity> optional = smsHistoryRepository.findTopByPhoneNumberOrderByCreatedDateDesc(phoneNumber);
        if (optional.isEmpty()) {
            throw new AppBadException("Invalid phone number");
        }
        return optional.get();
    }
}
