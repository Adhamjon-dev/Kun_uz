package dasturlash.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.*;
import dasturlash.uz.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Long sharedCount;

    private String imageId;
    private AttachDTO image;

    private Integer regionId;
    private String regionKey;
    private String regionName;

    private Integer moderatorId;
    private String moderatorName;

    private Integer publisherId;

    private ArticleStatus status;

    private Integer readTime; // in second
    private Integer viewCount;
    private LocalDateTime publishedDate;
    private List<CategoryDTO> categoryList;
    private List<SectionDTO> sectionList;
    private List<TagDTO> tagList;

    private Integer likeCount;
    private Integer dislikeCount;
}
