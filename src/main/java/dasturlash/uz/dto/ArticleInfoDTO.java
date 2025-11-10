package dasturlash.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleInfoDTO {
    private String id;

    @NotNull(message = "title required")
    private String title;
    @NotNull(message = "description required")
    private String description;
    @NotNull(message = "content required")
    private String content;
    @NotNull(message = "imageId required")
    private Integer imageId;
    @NotNull(message = "regionId required")
    private Integer regionId;
    @NotNull(message = "readTime required")
    private Integer readTime;
    @NotNull(message = "categoryIdList required")
    private List<Integer> categoryIdList;
    @NotNull(message = "sectionIdList required")
    private List<Integer> sectionIdList;
}
