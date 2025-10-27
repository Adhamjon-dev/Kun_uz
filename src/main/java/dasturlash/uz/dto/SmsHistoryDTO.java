package dasturlash.uz.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SmsHistoryDTO {
    private String id;
    private String phoneNumber;
    private String body;
    private String code;
    private Integer attemptCount;
    private LocalDateTime createdDate;
}
