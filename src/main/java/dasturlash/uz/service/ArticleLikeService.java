package dasturlash.uz.service;

import dasturlash.uz.dto.ArticleLikeDTO;
import dasturlash.uz.entitiy.ArticleLikeEntity;
import dasturlash.uz.enums.EmotionEnum;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.ArticleLikeRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleLikeService {
    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    public ArticleLikeDTO create(String articleId, EmotionEnum emotion) {
        Optional<ArticleLikeEntity> optional = getByArticleIdAndProfileId(articleId);
        if (optional.isPresent()) {
            ArticleLikeEntity entity = optional.get();
            if (entity.getEmotion() == emotion) {
                throw new AppBadException("Article like already exists");
            }
            entity.setEmotion(emotion);
            entity.setCreatedDate(LocalDateTime.now());
            articleLikeRepository.save(entity);
            return toDTO(entity);
        }
        ArticleLikeEntity articleLikeEntity = new ArticleLikeEntity();
        articleLikeEntity.setArticleId(articleId);
        articleLikeEntity.setEmotion(emotion);
        articleLikeEntity.setCreatedDate(LocalDateTime.now());
        articleLikeEntity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        articleLikeRepository.save(articleLikeEntity);
        return toDTO(articleLikeEntity);
    }

    public Boolean remove(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        if (articleLikeRepository.deleteByArticleIdAndProfileId(articleId, profileId) == 0) {
            throw new AppBadException("Article like not found");
        }
        return true;
    }

    private Optional<ArticleLikeEntity> getByArticleIdAndProfileId(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return articleLikeRepository.getByArticleIdAndProfileId(articleId, profileId);
    }

    private ArticleLikeDTO toDTO(ArticleLikeEntity entity) {
        ArticleLikeDTO dto = new ArticleLikeDTO();
        dto.setId(entity.getId());
        dto.setArticleId(entity.getArticleId());
        dto.setEmotion(entity.getEmotion());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }
}
