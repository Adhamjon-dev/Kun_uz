package dasturlash.uz.service;

import dasturlash.uz.dto.SmsHistoryDTO;
import dasturlash.uz.entitiy.SmsHistoryEntity;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

    public List<SmsHistoryDTO> getSmsDtoByPhone(String phone) {
        Iterable<SmsHistoryEntity> entities = smsHistoryRepository.findByPhoneNumber(phone);
        List<SmsHistoryDTO> list = new ArrayList<>();
        entities.forEach(entity -> list.add(toDto(entity)));
        return list;
    }

    public List<SmsHistoryDTO> getSmsDtoByDate(LocalDate date) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
        Iterable<SmsHistoryEntity> entities = smsHistoryRepository.findByDate(start, end);
        List<SmsHistoryDTO> list = new ArrayList<>();
        entities.forEach(entity -> list.add(toDto(entity)));
        return list;
    }

    public PageImpl<SmsHistoryDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SmsHistoryEntity> result = smsHistoryRepository.findAll(pageable);

        Iterable<SmsHistoryEntity> entities = result.getContent();
        long totalCount = result.getTotalElements();

        List<SmsHistoryDTO> dtoList = new ArrayList<>();
        entities.forEach(entity -> {dtoList.add(toDto(entity));});
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private SmsHistoryDTO toDto(SmsHistoryEntity smsHistoryEntity) {
        SmsHistoryDTO smsHistoryDTO = new SmsHistoryDTO();
        smsHistoryDTO.setId(smsHistoryEntity.getId());
        smsHistoryDTO.setPhoneNumber(smsHistoryEntity.getPhoneNumber());
        smsHistoryDTO.setBody(smsHistoryEntity.getBody());
        smsHistoryDTO.setCode(smsHistoryEntity.getCode());
        smsHistoryDTO.setCreatedDate(smsHistoryEntity.getCreatedDate());
        return smsHistoryDTO;
    }
}
