package dasturlash.uz.controller;

import dasturlash.uz.dto.SectionDTO;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @PostMapping({"", "/"})
    public ResponseEntity<SectionDTO> create(@Valid @RequestBody SectionDTO dto) {
        return ResponseEntity.ok(sectionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id, @Valid @RequestBody SectionDTO dto) {
        return ResponseEntity.ok(sectionService.update(dto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sectionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<SectionDTO>> getAll() {
        return ResponseEntity.ok(sectionService.getAll());
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<SectionDTO>> getAllPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "size", defaultValue = "2") int size) {
        return ResponseEntity.ok(sectionService.getAllPagination(page-1, size));
    }

    @GetMapping("/lang")
    public ResponseEntity<List<LanguageMapper>> getByLanguages(@RequestParam("language") String language) {
        return ResponseEntity.ok(sectionService.getByLanguage(language));
    }
}
