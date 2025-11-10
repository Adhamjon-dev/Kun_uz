package dasturlash.uz.service;

import dasturlash.uz.dto.ArticleInfoDTO;
import dasturlash.uz.entitiy.article.ArticleEntity;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.ArticleRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    ArticleSectionService articleSectionService;
    @Autowired
    ArticleCategoryService articleCategoryService;

    public ArticleInfoDTO create(ArticleInfoDTO dto) {
        ArticleEntity entity = new ArticleEntity();
        entity.setContent(dto.getContent());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setImageId(dto.getImageId());
        entity.setModeratorId(SpringSecurityUtil.getCurrentUserId());
        entity.setReadTime(dto.getReadTime());
        entity.setRegionId(dto.getRegionId());
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity);

        articleCategoryService.create(entity.getId(), dto.getCategoryIdList());
        articleSectionService.create(entity.getId(), dto.getSectionIdList());

        dto.setId(entity.getId());
        return dto;
    }

    public ArticleInfoDTO update(String articleId, ArticleInfoDTO newDto) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndVisibleTrue(articleId);
        if (optional.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        ArticleEntity entity = optional.get();
        entity.setContent(newDto.getContent());
        entity.setTitle(newDto.getTitle());
        entity.setDescription(newDto.getDescription());
        entity.setImageId(newDto.getImageId());
        entity.setModeratorId(SpringSecurityUtil.getCurrentUserId());
        entity.setReadTime(newDto.getReadTime());
        entity.setRegionId(newDto.getRegionId());
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity);

        articleCategoryService.update(entity.getId(), newDto.getCategoryIdList());
        articleSectionService.update(entity.getId(), newDto.getSectionIdList());

        newDto.setId(entity.getId());
        return newDto;
    }

    public String delete(Integer id) {
        articleRepository.updateVisibleById(id);
        return "success";
    }

    public String changeStatus(String articleId, ArticleStatus status) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndVisibleTrue(articleId);
        if (optional.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        ArticleEntity entity = optional.get();
        entity.setStatus(status);
        articleRepository.save(entity);
        return "Successfully updated";
    }
}