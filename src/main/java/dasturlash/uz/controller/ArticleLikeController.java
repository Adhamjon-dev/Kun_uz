package dasturlash.uz.controller;

import dasturlash.uz.dto.ArticleLikeDTO;
import dasturlash.uz.service.ArticleLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dasturlash.uz.enums.EmotionEnum.DISLIKE;
import static dasturlash.uz.enums.EmotionEnum.LIKE;

@RestController
@RequestMapping("/api/v1/article_like")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("like/{articleId}")
    public ResponseEntity<ArticleLikeDTO> like(@PathVariable("articleId") String articleId) {
        return ResponseEntity.ok(articleLikeService.create(articleId, LIKE));
    }

    @PostMapping("dislike/{articleId}")
    public ResponseEntity<ArticleLikeDTO> dislike(@PathVariable("articleId") String articleId) {
        return ResponseEntity.ok(articleLikeService.create(articleId, DISLIKE));
    }

    @DeleteMapping("delete/{articleId}")
    public ResponseEntity<Boolean> delete(@PathVariable("articleId") String articleId) {
        return ResponseEntity.ok(articleLikeService.remove(articleId));
    }
}