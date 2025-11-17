package dasturlash.uz.dto.comment;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CommentFilterDTO {
    private Integer id;
    private Integer profileId;
    private String articleId;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
}
