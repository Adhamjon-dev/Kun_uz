package dasturlash.uz.entitiy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sms_history")
public class SmsHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "code")
    private String code;

    @Column(name = "attempt_count")
    private Integer attemptCount = 0;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}