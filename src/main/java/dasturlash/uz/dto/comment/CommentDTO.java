package dasturlash.uz.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.dto.profile.ProfileDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    Integer id;
    LocalDateTime createdDate;
    LocalDateTime updateDate;
    String content;
    Integer profileId;
    String profileName;
    String profileSurname;
    String profileImageId;
    String profileImageUrl;
    String articleId;
    String articleTitle;
    Integer replyId;
    Boolean visible;
    Long likeCount;
    Long dislikeCount;
}
