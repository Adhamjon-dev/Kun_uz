package dasturlash.uz.controller;

import dasturlash.uz.dto.RegionDTO;
import dasturlash.uz.dto.TagDTO;
import dasturlash.uz.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({"", "/"})
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagDTO dto) {
        return ResponseEntity.ok(tagService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"getAll"})
    public ResponseEntity<List<TagDTO>> getAll() {
        return ResponseEntity.ok(tagService.getAll());
    }
}
