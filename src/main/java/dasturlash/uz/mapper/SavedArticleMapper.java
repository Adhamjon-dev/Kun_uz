package dasturlash.uz.mapper;

import java.time.LocalDateTime;

public interface SavedArticleMapper {
    Integer getId();
    String getArticleId();
    String getArticleTitle();
    String getArticleDescription();
    String getArticleImageId();
    LocalDateTime getSavedDate();
}
