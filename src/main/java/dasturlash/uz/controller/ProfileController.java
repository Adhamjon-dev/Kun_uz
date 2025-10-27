package dasturlash.uz.controller;

import dasturlash.uz.dto.profile.*;
import dasturlash.uz.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileDTO dto) {
        return ResponseEntity.ok(profileService.create(dto));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Boolean> updateAdmin(@PathVariable Integer id, @Valid @RequestBody UpdateProfileAdmin dto) {
        return ResponseEntity.ok(profileService.updateAdmin(id, dto));
    }

    @PutMapping("/own/{id}")
    public ResponseEntity<Boolean> updateOwn(@PathVariable Integer id, @Valid @RequestBody UpdateProfileOwn dto) {
        return ResponseEntity.ok(profileService.updateOwn(id, dto));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<Boolean> updateOwn(@PathVariable Integer id, @Valid @RequestBody ProfileUpdatePasswordDTO dto) {
        return ResponseEntity.ok(profileService.updatePassword(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        profileService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ProfileDTO>> getAllPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "size", defaultValue = "2") int size) {
        return ResponseEntity.ok(profileService.pagination(page-1, size));
    }

    @PostMapping("/filter")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDTO filter,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "size", defaultValue = "2") int size) {
        return ResponseEntity.ok(profileService.filter(filter, page-1, size));
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(profileService.get(id));
    }
}
