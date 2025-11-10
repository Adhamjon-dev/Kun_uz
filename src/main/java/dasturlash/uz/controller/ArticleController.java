package dasturlash.uz.controller;

import dasturlash.uz.dto.ArticleInfoDTO;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping({"", "/"})
    public ResponseEntity<ArticleInfoDTO> create(@Valid @RequestBody ArticleInfoDTO dto) {
        return ResponseEntity.ok(articleService.create(dto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleInfoDTO> update(@PathVariable String id,
                                          @Valid @RequestBody ArticleInfoDTO dto) {
        return ResponseEntity.ok(articleService.update(id, dto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping({"/change"})
    public ResponseEntity<String> changeStatus(@RequestParam("id") String id,
                                                             @RequestParam("status") ArticleStatus status) {
        return ResponseEntity.ok(articleService.changeStatus(id, status));
    }
}
