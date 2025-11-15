package dasturlash.uz.dto.article;

import dasturlash.uz.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ArticleAdminFilterDTO {
    private String title;
    private Integer regionId;
    private Integer sectionId;
    private Integer categoryId;
    private LocalDate publishedDateFrom;
    private LocalDate publishedDateTo;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
    private Integer moderatorId;
    private Integer publisherId;
    private ArticleStatus status;
}
