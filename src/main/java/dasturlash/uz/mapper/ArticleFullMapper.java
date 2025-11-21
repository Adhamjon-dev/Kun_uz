package dasturlash.uz.mapper;

public interface ArticleFullMapper {
    String getId();
    String getTitle();
    String getDescription();
    String getContent();

    Long getSharedCount();
    Integer getViewCount();
    Integer getReadTime();

    String getRegionName();
    String getRegionKey();

    Integer getModeratorId();
    String getModeratorName();

    String getSections();
    String getCategories();
    String getTags();

    Integer getLikeCount();
    Integer getDislikeCount();
}
