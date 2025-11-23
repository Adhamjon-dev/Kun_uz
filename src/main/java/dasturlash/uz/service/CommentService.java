package dasturlash.uz.service;

import dasturlash.uz.dto.CustomFilterResultDTO;
import dasturlash.uz.dto.comment.CommentCreateDTO;
import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.dto.comment.CommentFilterDTO;
import dasturlash.uz.entitiy.CommentEntity;
import dasturlash.uz.exp.AppAccessDeniedException;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.CommentMapper;
import dasturlash.uz.repository.CommentRepository;
import dasturlash.uz.repository.CustomCommentRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    CustomCommentRepository customCommentRepository;
    @Autowired
    AttachService attachService;

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

    public List<CommentDTO> getByArticleIdAndProfileId(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        List<CommentMapper> mapperList = commentRepository.getByArticleIdAndProfileId(articleId, profileId);

        List<CommentDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toDTO(mapper)));
        return dtoList;
    }

    public List<CommentDTO> getByArticleId(String articleId) {
        List<CommentMapper> mapperList = commentRepository.getByArticleId(articleId);

        List<CommentDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toDTO(mapper)));
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

    public PageImpl<CommentDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<CommentMapper> resultPage = commentRepository.pagination(pageable);

        List<CommentMapper> mapperList = resultPage.getContent();
        long totalCount = resultPage.getTotalElements();

        List<CommentDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> dtoList.add(toDTO(mapper)));
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }

    private CommentDTO toDTO(Object[] objects) {
        CommentDTO dto = new CommentDTO();
        dto.setId((Integer) objects[0]);
        dto.setCreatedDate((LocalDateTime) objects[1]);
        if (objects[2] != null) {
            dto.setUpdateDate((LocalDateTime) objects[2]);
        }
        dto.setContent((String) objects[3]);
        dto.setProfileId((Integer) objects[4]);
        dto.setProfileName((String) objects[5]);
        dto.setProfileSurname((String) objects[6]);
        dto.setArticleId((String) objects[7]);
        dto.setArticleTitle((String) objects[8]);
        if (objects[9] != null) {
            dto.setReplyId((Integer) objects[9]);
        }
        dto.setVisible((Boolean) objects[10]);
        dto.setLikeCount((Long) objects[11]);
        dto.setDislikeCount((Long) objects[12]);
        return dto;
    }

    private CommentDTO toDTO(CommentMapper mapper) {
        CommentDTO dto = new CommentDTO();
        dto.setId(mapper.getId());
        dto.setCreatedDate(mapper.getCreatedDate());
        dto.setUpdateDate(mapper.getUpdateDate());
        dto.setContent(mapper.getContent());
        dto.setArticleId(mapper.getArticleId());
        dto.setArticleTitle(mapper.getArticleTitle());
        dto.setProfileId(mapper.getProfileId());
        dto.setProfileName(mapper.getProfileName());
        dto.setProfileSurname(mapper.getProfileSurname());
        if (mapper.getProfileImageId() != null) {
            dto.setProfileImage(attachService.openDTO(mapper.getProfileImageId()));
        }
        dto.setLikeCount(mapper.getLikeCount());
        dto.setDislikeCount(mapper.getDislikeCount());
        dto.setVisible(mapper.getVisible());
        dto.setReplyId(mapper.getReplyId());
        return dto;
    }

    private CommentDTO toDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdateDate(entity.getUpdateDate());
        dto.setReplyId(entity.getReplyId());

        return dto;
    }

    public CommentEntity get(Integer id) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Comment not found");
        }
        return optional.get();
    }
}
