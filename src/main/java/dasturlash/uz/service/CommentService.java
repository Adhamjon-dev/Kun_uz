package dasturlash.uz.service;

import dasturlash.uz.dto.CustomFilterResultDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.dto.comment.CommentCreateDTO;
import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.dto.comment.CommentFilterDTO;
import dasturlash.uz.dto.profile.ProfileDTO;
import dasturlash.uz.entitiy.CommentEntity;
import dasturlash.uz.exp.AppAccessDeniedException;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.CommentRepository;
import dasturlash.uz.repository.CustomCommentRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static dasturlash.uz.enums.ProfileRoleEnum.ROLE_ADMIN;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    CustomCommentRepository customCommentRepository;

    public CommentDTO create(CommentCreateDTO createDTO) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(createDTO.getContent());
        entity.setArticleId(createDTO.getArticleId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        if (createDTO.getReplyId() != null) {
            entity.setReplyId(createDTO.getReplyId());
        }

        commentRepository.save(entity);
        return toDTO(entity);
    }

    public CommentDTO update(Integer commentId, CommentCreateDTO updateDTO) {
        CommentEntity entity = get(commentId);
        if (!entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())) {
            throw new  AppAccessDeniedException("Access denied");
        }
        entity.setContent(updateDTO.getContent());
        entity.setArticleId(updateDTO.getArticleId());
        entity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(entity);

        return toDTO(entity);
    }

    public Boolean delete(Integer commentId) {
        CommentEntity entity = get(commentId);
        if (!entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId()) & !SpringSecurityUtil.checkRoleExist(ROLE_ADMIN)) {
            throw new  AppAccessDeniedException("Access denied");
        }
        entity.setVisible(false);
        commentRepository.save(entity);
        return true;
    }

    public CommentDTO getById(Integer id) {
        CommentEntity entity = get(id);
        CommentDTO commentDTO = toDTO(entity);
        ProfileDTO profileDTO = profileService.getById(entity.getProfileId());
        commentDTO.setProfile(profileDTO);
        return commentDTO;
    }

    public List<CommentDTO> getByArticleId(Integer articleId) {
        List<CommentDTO> dtoList = commentRepository.getByArticleId(articleId);
        dtoList.forEach(dto -> dto.setProfile(profileService.getById(dto.getProfileId())));
        return dtoList;
    }

    public PageImpl<CommentDTO> filter(CommentFilterDTO filter, int page, int size) {
        CustomFilterResultDTO<Object[]> result = customCommentRepository.filter(filter, page, size);
        List<Object[]> objList = result.getContent();
        long totalCount = result.getTotalCount();

        List<CommentDTO> dtoList = new LinkedList<>();
        objList.forEach(objects -> dtoList.add(toDTO(objects)));
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }

    private CommentDTO toDTO(Object[] objects) {
        CommentDTO dto = new CommentDTO();
        dto.setId((Integer) objects[0]);
        dto.setCreatedDate((LocalDateTime) objects[1]);
        dto.setUpdateDate((LocalDateTime) objects[2]);
        dto.setContent((String) objects[3]);
        dto.setProfile(getProfile(objects));
        dto.setArticle(getArticle(objects));
        if (objects[9] != null) {
            dto.setReplyId((Integer) objects[9]);
        }

        return dto;
    }

    private ProfileDTO getProfile(Object[] objects) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId((Integer) objects[4]);
        profileDTO.setName((String) objects[5]);
        profileDTO.setSurname((String) objects[6]);
        return profileDTO;
    }

    private ArticleDTO getArticle(Object[] objects) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(objects[7].toString());
        articleDTO.setTitle((String) objects[8]);
        return articleDTO;
    }

    private CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdateDate(entity.getUpdateDate());

        return dto;
    }

    public CommentEntity get(Integer id) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        return optional.get();
    }
}
