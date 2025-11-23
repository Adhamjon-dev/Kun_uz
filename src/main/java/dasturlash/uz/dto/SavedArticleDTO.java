package dasturlash.uz.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SavedArticleDTO {
    private Integer id;
    private String articleId;
    private String articleTitle;
    private String articleDescription;
    private AttachDTO articleImage;
    private LocalDateTime savedDate;
}
