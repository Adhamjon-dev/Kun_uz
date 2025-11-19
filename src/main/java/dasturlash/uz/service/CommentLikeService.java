package dasturlash.uz.service;

import dasturlash.uz.dto.CommentLikeDTO;
import dasturlash.uz.entitiy.CommentLikeEntity;
import dasturlash.uz.enums.EmotionEnum;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.CommentLikeRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public CommentLikeDTO create(Integer commentId, EmotionEnum emotion) {
        Optional<CommentLikeEntity> optional = getByCommentIdAndProfileId(commentId);
        if (optional.isPresent()) {
            CommentLikeEntity entity = optional.get();
            if (entity.getEmotion() == emotion) {
                throw new AppBadException("Comment like already exists");
            }
            entity.setEmotion(emotion);
            entity.setCreatedDate(LocalDateTime.now());
            commentLikeRepository.save(entity);
            return toDTO(entity);
        }
        CommentLikeEntity entity = new CommentLikeEntity();
        entity.setCommentId(commentId);
        entity.setEmotion(emotion);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        commentLikeRepository.save(entity);
        return toDTO(entity);
    }

    public Boolean remove(Integer commentId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        if (commentLikeRepository.deleteByCommentIdAndProfileId(commentId, profileId) == 0) {
            throw new AppBadException("Comment like not found");
        }
        return true;
    }

    private Optional<CommentLikeEntity> getByCommentIdAndProfileId(Integer commentId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return commentLikeRepository.getByCommentIdAndProfileId(commentId, profileId);
    }

    private CommentLikeDTO toDTO(CommentLikeEntity entity) {
        CommentLikeDTO dto = new CommentLikeDTO();
        dto.setId(entity.getId());
        dto.setCommentId(entity.getCommentId());
        dto.setEmotion(entity.getEmotion());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }
}
