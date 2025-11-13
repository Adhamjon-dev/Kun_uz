package dasturlash.uz.service;

import dasturlash.uz.dto.RegionDTO;
import dasturlash.uz.dto.article.ArticleCreateDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.entitiy.article.ArticleEntity;
import dasturlash.uz.enums.AppLanguageEnum;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.ArticleShortInfo;
import dasturlash.uz.repository.ArticleRepository;
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

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    ArticleSectionService articleSectionService;
    @Autowired
    ArticleCategoryService articleCategoryService;
    @Autowired
    RegionService  regionService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SectionService sectionService;
    @Autowired
    ProfileService profileService;

    public ArticleDTO create(ArticleCreateDTO createDTO) {
        ArticleEntity entity = new ArticleEntity();
        toEntity(createDTO, entity);
        // Set default values
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setViewCount(0);
        entity.setSharedCount(0L);
        entity.setModeratorId(SpringSecurityUtil.getCurrentUserId());
        // save
        articleRepository.save(entity);

        articleCategoryService.merge(entity.getId(), createDTO.getCategoryList());
        articleSectionService.merge(entity.getId(), createDTO.getSectionList());

        return toDTO(entity);
    }

    public ArticleDTO update(String articleId, ArticleCreateDTO createDTO) {
        ArticleEntity entity = get(articleId);
        toEntity(createDTO, entity);
        articleRepository.save(entity);

        articleCategoryService.merge(entity.getId(), createDTO.getCategoryList());
        articleSectionService.merge(entity.getId(), createDTO.getSectionList());

        return toDTO(entity);
    }

    public String delete(String articleId) {
        ArticleEntity entity = get(articleId);

        if (!entity.getModeratorId().equals(SpringSecurityUtil.getCurrentUserId())) {
            if(!SpringSecurityUtil.checkRoleExist(ProfileRoleEnum.ROLE_ADMIN)){
                throw new AppBadException("Access limit");
            }
        }
        entity.setVisible(Boolean.FALSE);
        articleRepository.save(entity);
        return "Article deleted";
    }

    public String changeStatus(String articleId, ArticleStatus status) {
//        int effectedRows = articleRepository.changeStatus(articleId, status);
//        if (effectedRows > 0) {
//
//            return "Article status change";
//        } else {
//            return "Something went wrong";
//        }

        ArticleEntity entity = get(articleId);
        entity.setStatus(status);
        entity.setPublishedDate(LocalDateTime.now());
        entity.setPublisherId(SpringSecurityUtil.getCurrentUserId());
        articleRepository.save(entity);
        return "Article status change";
    }

    public PageImpl<ArticleDTO> getBySectionId(Integer sectionId, int limit, int page, int size) {
        List<ArticleShortInfo> resultList = articleRepository.getBySectionId(sectionId, limit);

        return getPage(resultList, page, size);
    }

    public PageImpl<ArticleDTO> getLas12PublishedArticle(List<String> exceptIdList, int page, int size) {
        List<ArticleShortInfo> resultList = articleRepository.getPublishedArticleListExceptIds(exceptIdList, 12);

        return getPage(resultList, page, size);
    }

    public PageImpl<ArticleDTO> getLastCategoryId(Integer categoryId, int limit, int page, int size) {
        List<ArticleShortInfo> list = articleRepository.getByCategoryId(categoryId, limit);

        return getPage(list, page, size);
    }

    private PageImpl<ArticleDTO> getPage(List<ArticleShortInfo> list, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        int start = page * size;
        int end = Math.min(start + pageable.getPageSize(), list.size());

        List<ArticleShortInfo> responseList = new LinkedList<>();
        if (end > start) {
            responseList = list.subList(start, end);
        }

        List<ArticleDTO> content = new LinkedList<>();
        responseList.forEach(mapper -> content.add(toDTO(mapper)));

        return new PageImpl<>(content, pageable, list.size());
    }

    public PageImpl<ArticleDTO> getLastRegionId(Integer regionId, int limit, int page, int size) {
        List<ArticleShortInfo> list = articleRepository.getByRegionId(regionId, limit);

        return getPage(list, page, size);
    }

    //  first way
    public ArticleDTO getByIdAndLang(String id, AppLanguageEnum lang) {
        Optional<ArticleEntity> articleOptional = articleRepository.findById(id);
        if (articleOptional.isEmpty()) {
            throw new AppBadException("Article Not Found");
        }
        ArticleEntity article = articleOptional.get();

        ArticleDTO result = new ArticleDTO();
        result.setId(article.getId());
        result.setTitle(article.getTitle());
        result.setContent(article.getContent());
        result.setDescription(article.getDescription());
        result.setSharedCount(article.getSharedCount());
        result.setViewCount(article.getViewCount());
        result.setPublishedDate(article.getPublishedDate());
        // region(key,name)
        RegionDTO region = regionService.getByIdAndLang(article.getRegionId(), lang);
        result.setRegion(region);
        // categoryList[{key,name},{}]
        result.setCategoryList(categoryService.getCategoryListByArticleId(article.getId(), lang));
        // sectionList[{id,name},{id,name}]
        result.setSectionList(sectionService.getSectionListByArticleId(article.getId(), lang));
        // moderator -
        result.setModerator(profileService.getById(article.getModeratorId()));

        return result;
    }

    public List<ArticleDTO> getByLast4ArticleBySectionId(Integer sectionId, String exceptArticleId) {
        List<ArticleShortInfo> resultList = articleRepository.getBySectionIdAndExceptId(exceptArticleId, sectionId);
        List<ArticleDTO> responseList = new LinkedList<>();
        resultList.forEach(mapper -> responseList.add(toDTO(mapper)));
        return responseList;
    }

    private void toEntity(ArticleCreateDTO dto, ArticleEntity entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
//        entity.setImageId(dto.getImageId());
        entity.setRegionId(dto.getRegionId());
        entity.setReadTime(dto.getReadTime());
    }

    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setSharedCount(entity.getSharedCount());
        dto.setReadTime(entity.getReadTime());
        dto.setViewCount(entity.getViewCount());
        dto.setStatus(entity.getStatus());
        dto.setImageId(entity.getImageId());
        dto.setRegionId(entity.getRegionId());
        dto.setPublishedDate(entity.getPublishedDate());
        return dto;
    }

    private ArticleDTO toDTO(ArticleShortInfo mapper) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setDescription(mapper.getDescription());
//        dto.setImage(attachService.openDTO(mapper.getId()));
        dto.setPublishedDate(mapper.getPublishedDate());
        return dto;
    }

    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        return optional.get();
    }
}