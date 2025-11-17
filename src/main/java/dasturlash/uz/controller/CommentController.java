package dasturlash.uz.controller;

import dasturlash.uz.dto.comment.CommentCreateDTO;
import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.dto.comment.CommentFilterDTO;
import dasturlash.uz.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR', 'PUBLISH')")
    @PostMapping({"", "/"})
    public ResponseEntity<CommentDTO> create(@Valid @RequestBody CommentCreateDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR', 'PUBLISH')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> update(@PathVariable Integer id,
                                          @Valid @RequestBody CommentCreateDTO dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR', 'PUBLISH')")
    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("get/{id}")
    public ResponseEntity<CommentDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("article/{id}")
    public ResponseEntity<List<CommentDTO>> getByArticleId(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getByArticleId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("filter")
    public ResponseEntity<PageImpl<CommentDTO>> filter(@RequestBody CommentFilterDTO dto,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.filter(dto,page - 1,size));
    }
}
