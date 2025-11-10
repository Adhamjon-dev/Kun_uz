package dasturlash.uz.entitiy.article;

import dasturlash.uz.entitiy.ImageEntity;
import dasturlash.uz.entitiy.ProfileEntity;
import dasturlash.uz.entitiy.RegionEntity;
import dasturlash.uz.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = 'description', columnDefinition = "TEXT")
    private String description;

    @Column(name = 'content', columnDefinition = "TEXT")
    private String content;

    @Column(name = "shared_count", columnDefinition = "int default 0")
    private Integer sharedCount = 0;

    @Column(name = "image_id")
    private Integer imageId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private ImageEntity image;

    @Column(name = "region_id")
    private Integer regionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id",  insertable = false, updatable = false)
    private RegionEntity region;

    @Column(name = "moderator_id")
    private Integer moderatorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private ProfileEntity moderator;

    @Column(name = "publisher_id")
    private Integer publisherId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id",  insertable = false, updatable = false)
    private ProfileEntity publisher;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @Column(name = "read_time")
    private Integer readTime;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "visible", nullable = false)
    private Boolean visible = Boolean.TRUE;

    @Column(name = "view_count", columnDefinition = "int default 0")
    private Integer viewCount = 0;
}
