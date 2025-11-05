package dasturlash.uz.controller;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.dto.auth.JwtDTO;
import dasturlash.uz.enums.AppLanguageEnum;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.exp.AppAccessDeniedException;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.service.CategoryService;
import dasturlash.uz.util.JwtUtil;
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
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto,
                                              @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }

        return ResponseEntity.ok(categoryService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id,
                                          @Valid @RequestBody CategoryDTO dto,
                                          @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }

        return ResponseEntity.ok(categoryService.update(dto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
                                       @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<CategoryDTO>> getAll(@RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/lang")
    public ResponseEntity<List<LanguageMapper>> getByLang(@RequestHeader(name = "Accept-Language", defaultValue = "uz") AppLanguageEnum language) {
        return ResponseEntity.ok(categoryService.getAllByLang(language));
    }
}
