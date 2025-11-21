package dasturlash.uz.mapper;

import java.time.LocalDateTime;

public interface CommentMapper {
    Integer getId();

    LocalDateTime getCreatedDate();

    LocalDateTime getUpdateDate();

    String getContent();

    String getArticleId();
    String getArticleTitle();

    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();
    String getProfileImageId();
    String getProfileImageUrl();

    Integer getReplyId();
    Boolean getVisible();

    Long getLikeCount();
    Long getDislikeCount();
}