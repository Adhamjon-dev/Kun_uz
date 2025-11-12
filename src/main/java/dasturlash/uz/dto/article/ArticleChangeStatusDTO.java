package dasturlash.uz.dto.article;

import dasturlash.uz.enums.ArticleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleChangeStatusDTO {
    @NotNull(message = "Status required")
    private ArticleStatus status;
}