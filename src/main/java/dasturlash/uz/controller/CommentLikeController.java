package dasturlash.uz.controller;

import dasturlash.uz.dto.CommentLikeDTO;
import dasturlash.uz.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dasturlash.uz.enums.EmotionEnum.DISLIKE;
import static dasturlash.uz.enums.EmotionEnum.LIKE;

@RestController
@RequestMapping("/api/v1/comment_like")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("like/{commentId}")
    public ResponseEntity<CommentLikeDTO> like(@PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok(commentLikeService.create(commentId, LIKE));
    }

    @PostMapping("dislike/{commentId}")
    public ResponseEntity<CommentLikeDTO> dislike(@PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok(commentLikeService.create(commentId, DISLIKE));
    }

    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<Boolean> delete(@PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok(commentLikeService.remove(commentId));
    }
}
