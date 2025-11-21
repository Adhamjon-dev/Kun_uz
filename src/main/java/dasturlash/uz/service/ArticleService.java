package dasturlash.uz.service;

import dasturlash.uz.dto.*;
import dasturlash.uz.dto.article.ArticleAdminFilterDTO;
import dasturlash.uz.dto.article.ArticleCreateDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.dto.article.ArticleFilterDTO;
import dasturlash.uz.entitiy.article.ArticleEntity;
import dasturlash.uz.enums.AppLanguageEnum;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.ArticleFullMapper;
import dasturlash.uz.mapper.ArticleShortInfo;
import dasturlash.uz.repository.ArticleRepository;
import dasturlash.uz.repository.CustomArticleRepository;
import dasturlash.uz.util.FileUtil;
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
    ArticleTagService articleTagService;
    @Autowired
    CustomArticleRepository customArticleRepository;
    @Autowired
    AttachService attachService;

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
        articleTagService.merge(entity.getId(), createDTO.getTagList());

        return toDTO(entity);
    }

    public ArticleDTO update(String articleId, ArticleCreateDTO createDTO) {
        ArticleEntity entity = get(articleId);
        toEntity(createDTO, entity);
        articleRepository.save(entity);

        articleCategoryService.merge(entity.getId(), createDTO.getCategoryList());
        articleSectionService.merge(entity.getId(), createDTO.getSectionList());
        articleTagService.merge(entity.getId(), createDTO.getTagList());

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

    public PageImpl<ArticleDTO> getLastByTagName(String tagName, int limit, int page, int size) {
        List<ArticleShortInfo> list = articleRepository.getByTagName(tagName, limit);

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
        ArticleFullMapper mapper = articleRepository.getByArticleIdAndLang(id, lang.name());

        if (mapper == null) {
            throw new AppBadException("Article not found");
        }

        List<CategoryDTO> categories;
        List<SectionDTO> sections;
        List<TagDTO> tags;
        categories = FileUtil.read(mapper.getCategories(), CategoryDTO.class);
        sections = FileUtil.read(mapper.getSections(), SectionDTO.class);
        tags = FileUtil.read(mapper.getTags(), TagDTO.class);
        return toDTO(mapper, categories, sections, tags);
    }

    public List<ArticleDTO> getByLast4ArticleBySectionId(Integer sectionId, String exceptArticleId) {
        List<ArticleShortInfo> resultList = articleRepository.getBySectionIdAndExceptId(exceptArticleId, sectionId);
        List<ArticleDTO> responseList = new LinkedList<>();
        resultList.forEach(mapper -> responseList.add(toDTO(mapper)));
        return responseList;
    }

    public List<ArticleDTO> getViewTop4ArticleByArticleId(String exceptArticleId) {
        List<ArticleShortInfo> resultList = articleRepository.getViewTop4ArticleByExceptId(exceptArticleId);
        List<ArticleDTO> responseList = new LinkedList<>();
        resultList.forEach(mapper -> responseList.add(toDTO(mapper)));
        return responseList;
    }

    public Integer increaseViewCountByArticleId(String articleId) {
        ArticleEntity entity = getPublished(articleId);
        entity.setViewCount(entity.getViewCount() + 1);
        articleRepository.save(entity);

        return entity.getViewCount();
    }

    public Long increaseSharedCountByArticleId(String articleId) {
        ArticleEntity entity = getPublished(articleId);
        entity.setSharedCount(entity.getSharedCount() + 1);
        articleRepository.save(entity);

        return entity.getSharedCount();
    }

    public PageImpl<ArticleDTO> filterAny(ArticleFilterDTO filter, int page, int size) {
        CustomFilterResultDTO<Object[]> result = customArticleRepository.filterAny(filter, page, size);
        List<Object[]> objList = result.getContent();
        long totalCount = result.getTotalCount();

        List<ArticleDTO> dtoList = new LinkedList<>();
        objList.forEach(objects -> dtoList.add(toDTO(objects)));
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }

    public PageImpl<ArticleDTO> filterModerator(ArticleFilterDTO filter, int page, int size) {
        CustomFilterResultDTO<Object[]> result = customArticleRepository.filterModerator(filter, page, size);
        List<Object[]> objList = result.getContent();
        long totalCount = result.getTotalCount();

        List<ArticleDTO> dtoList = new LinkedList<>();
        objList.forEach(objects -> dtoList.add(toDTO(objects)));
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }

    public PageImpl<ArticleDTO> filterAdmin(ArticleAdminFilterDTO filter, int page, int size) {
        CustomFilterResultDTO<Object[]> result = customArticleRepository.filterAdmin(filter, page, size);
        List<Object[]> objList = result.getContent();
        long totalCount = result.getTotalCount();

        List<ArticleDTO> dtoList = new LinkedList<>();
        objList.forEach(objects -> dtoList.add(toDTO(objects)));
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }

    private ArticleDTO toDTO(Object[] objects) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(objects[0].toString());
        dto.setTitle(objects[1].toString());
        dto.setDescription(objects[2].toString());
//        dto.setImageId(objects[3].toString());
        dto.setPublishedDate((LocalDateTime) objects[4]);
        return dto;
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
        dto.setImage(attachService.openDTO(mapper.getImageId()));
        dto.setPublishedDate(mapper.getPublishedDate());
        return dto;
    }

    private ArticleDTO toDTO(ArticleFullMapper mapper, List<CategoryDTO> categories, List<SectionDTO> sections, List<TagDTO> tags) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setDescription(mapper.getDescription());
        dto.setContent(mapper.getContent());
        dto.setSharedCount(mapper.getSharedCount());
        dto.setViewCount(mapper.getViewCount());
        dto.setReadTime(mapper.getReadTime());
        dto.setRegionName(mapper.getRegionName());
        dto.setRegionKey(mapper.getRegionKey());
        dto.setModeratorId(mapper.getModeratorId());
        dto.setModeratorName(mapper.getModeratorName());
        dto.setCategoryList(categories);
        dto.setSectionList(sections);
        dto.setTagList(tags);
        dto.setLikeCount(mapper.getLikeCount());
        dto.setDislikeCount(mapper.getDislikeCount());
        return dto;
    }

    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        return optional.get();
    }

    public ArticleEntity getPublished(String id) {
        Optional<ArticleEntity> optional = articleRepository.findByIdAndVisibleTrueAndStatus(id, ArticleStatus.PUBLISHED);
        if (optional.isEmpty()) {
            throw new AppBadException("Article not found");
        }
        return optional.get();
    }
}