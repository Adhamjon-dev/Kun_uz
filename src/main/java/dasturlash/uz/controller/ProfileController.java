package dasturlash.uz.controller;

import dasturlash.uz.dto.auth.JwtDTO;
import dasturlash.uz.dto.profile.*;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.exp.AppAccessDeniedException;
import dasturlash.uz.service.ProfileService;
import dasturlash.uz.util.JwtUtil;
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
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileDTO dto,
                                             @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(profileService.create(dto));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Boolean> updateAdmin(@PathVariable Integer id,
                                               @Valid @RequestBody UpdateProfileAdminDTO dto,
                                               @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(profileService.updateAdmin(id, dto));
    }

    @PutMapping("/own/{id}")
    public ResponseEntity<Boolean> updateOwn(@PathVariable Integer id,
                                             @Valid @RequestBody UpdateProfileOwnDTO dto,
                                             @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);

        return ResponseEntity.ok(profileService.updateOwn(id, dto, jwtDTO));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<Boolean> updatePassword(@PathVariable Integer id,
                                                  @Valid @RequestBody ProfileUpdatePasswordDTO dto,
                                                  @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(profileService.updatePassword(id, dto, jwtDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
                                       @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        profileService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ProfileDTO>> getAllPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "size", defaultValue = "2") int size,
                                                                 @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(profileService.pagination(page-1, size));
    }

    @PostMapping("/filter")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDTO filter,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "size", defaultValue = "2") int size,
                                                       @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(profileService.filter(filter, page-1, size));
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable Integer id,
                                              @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(profileService.get(id));
    }
}
