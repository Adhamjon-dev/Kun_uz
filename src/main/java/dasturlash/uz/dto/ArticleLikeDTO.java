package dasturlash.uz.dto;

import dasturlash.uz.enums.EmotionEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleLikeDTO {
    private String id;
    private String articleId;
    private Integer profileId;
    private LocalDateTime createdDate;
    private EmotionEnum  emotion;
}
