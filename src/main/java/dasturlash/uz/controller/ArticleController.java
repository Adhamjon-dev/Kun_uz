package dasturlash.uz.controller;

import dasturlash.uz.dto.article.*;
import dasturlash.uz.enums.AppLanguageEnum;
import dasturlash.uz.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping({"", "/"})
    public ResponseEntity<ArticleDTO> create(@RequestBody @Valid ArticleCreateDTO dto) {
        return ResponseEntity.ok(articleService.create(dto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> update(@PathVariable String id,
                                             @Valid @RequestBody ArticleCreateDTO dto) {
        return ResponseEntity.ok(articleService.update(id, dto));
    }

    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PreAuthorize("hasRole('PUBLISH')")
    @PutMapping("/{articleId}/status")
    public ResponseEntity<String> changeStatus(@PathVariable("articleId") String articleId,
                                               @RequestBody @Valid ArticleChangeStatusDTO statusDTO) {
        return ResponseEntity.ok(articleService.changeStatus(articleId, statusDTO.getStatus()));
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<PageImpl<ArticleDTO>> getArticleBySectionId(@PathVariable("sectionId") Integer sectionId,
                                                         @RequestParam(value = "limit", defaultValue = "1") int limit,
                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getBySectionId(sectionId, limit, page - 1, size));
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<PageImpl<ArticleDTO>> getArticleByRegionId(@PathVariable("regionId") Integer regionId,
                                                                           @RequestParam(value = "limit", defaultValue = "1") int limit,
                                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getLastRegionId(regionId, limit, page - 1, size));
    }

    @GetMapping("/get/{articleId}")
    public ResponseEntity<ArticleDTO> getByIdAndLang(@PathVariable("articleId") String articleId,
                                                            @RequestHeader(name = "Accept-Language", defaultValue = "uz") AppLanguageEnum language) {
        return ResponseEntity.ok(articleService.getByIdAndLang(articleId, language));
    }

    @PostMapping("/last12")
    public ResponseEntity<PageImpl<ArticleDTO>> last12ArticleExceptGivenIdList(@RequestBody List<String> exceptIdList,
                                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getLas12PublishedArticle(exceptIdList, page - 1, size));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageImpl<ArticleDTO>> getArticleByCategoryId(@PathVariable("categoryId") Integer categoryId,
                                                                  @RequestParam(value = "limit", defaultValue = "5") int limit,
                                                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getLastCategoryId(categoryId, limit, page - 1, size));
    }

    @PostMapping("/section/{sectionId}/last4")
    public ResponseEntity<List<ArticleDTO>> last4BySectionId(@PathVariable("sectionId") Integer sectionId,
                                                             @RequestParam("exceptArticleId") String exceptArticleId) {
        return ResponseEntity.ok(articleService.getByLast4ArticleBySectionId(sectionId, exceptArticleId));
    }

    @PostMapping("/view_top4")
    public ResponseEntity<List<ArticleDTO>> getViewTop4(@RequestParam("exceptArticleId") String exceptArticleId) {
        return ResponseEntity.ok(articleService.getViewTop4ArticleByArticleId(exceptArticleId));
    }

    @GetMapping("/increase/view/{articleId}")
    public ResponseEntity<Integer> increaseViewCount(@PathVariable("articleId") String articleId) {
        return ResponseEntity.ok(articleService.increaseViewCountByArticleId(articleId));
    }

    @GetMapping("/increase/shared/{articleId}")
    public ResponseEntity<Long> increaseSharedCount(@PathVariable("articleId") String articleId) {
        return ResponseEntity.ok(articleService.increaseSharedCountByArticleId(articleId));
    }

    @GetMapping("/tagName/{tagName}")
    public ResponseEntity<PageImpl<ArticleDTO>> getArticleByTagName(@PathVariable("tagName") String tagName,
                                                                       @RequestParam(value = "limit", defaultValue = "5") int limit,
                                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getLastByTagName(tagName, limit, page - 1, size));
    }

    @PostMapping("user/filter")
    public ResponseEntity<PageImpl<ArticleDTO>> userFilter(@RequestBody ArticleFilterDTO filter,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.filterAny(filter,page - 1, size));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("moderator/filter")
    public ResponseEntity<PageImpl<ArticleDTO>> moderatorFilter(@RequestBody ArticleFilterDTO filter,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.filterModerator(filter,page - 1, size));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("admin/filter")
    public ResponseEntity<PageImpl<ArticleDTO>> adminFilter(@RequestBody ArticleAdminFilterDTO filter,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.filterAdmin(filter,page - 1, size));
    }
}
