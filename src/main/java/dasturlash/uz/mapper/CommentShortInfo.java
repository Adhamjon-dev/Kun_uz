package dasturlash.uz.mapper;

import java.time.LocalDateTime;

public interface CommentShortInfo {
    Integer getId();

    LocalDateTime getCreatedDate();

    LocalDateTime getUpdateDate();

    String getContent();

    String getArticleId();

    Integer getProfileId();}
