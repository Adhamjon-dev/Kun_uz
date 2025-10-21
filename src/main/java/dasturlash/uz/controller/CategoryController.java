package dasturlash.uz.controller;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping({"", "/"})
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id, @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.update(dto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/lang")
    public ResponseEntity<List<LanguageMapper>> getByLanguages(@RequestParam("language") String language) {
        return ResponseEntity.ok(categoryService.getByLanguage(language));
    }
}
