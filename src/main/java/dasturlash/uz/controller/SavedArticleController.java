package dasturlash.uz.controller;

import dasturlash.uz.dto.SavedArticleDTO;
import dasturlash.uz.service.SavedArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/saved_article")
public class SavedArticleController {
    @Autowired
    SavedArticleService savedArticleService;

    @PostMapping("/{article_id}")
    public ResponseEntity<Boolean> create(@PathVariable("article_id") String articleId) {
        return ResponseEntity.ok(savedArticleService.save(articleId));
    }

    @DeleteMapping("/{article_id}")
    public ResponseEntity<Boolean> delete(@PathVariable("article_id") String articleId) {
        return ResponseEntity.ok(savedArticleService.delete(articleId));
    }

    @GetMapping("/get")
    public ResponseEntity<List<SavedArticleDTO>> getByProfileId() {
        return ResponseEntity.ok(savedArticleService.getByProfileId());
    }
}