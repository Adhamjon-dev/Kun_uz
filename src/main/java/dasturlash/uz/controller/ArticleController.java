package dasturlash.uz.controller;

import dasturlash.uz.dto.article.ArticleChangeStatusDTO;
import dasturlash.uz.dto.article.ArticleCreateDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PreAuthorize("hasRole('MODERATOR')")
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
    public ResponseEntity<List<ArticleDTO>> getArticleBySectionId(@PathVariable("sectionId") Integer sectionId,
                                                         @RequestParam(value = "limit", defaultValue = "1") int limit) {
        return ResponseEntity.ok(articleService.getBySectionId(sectionId, limit));
    }
}
