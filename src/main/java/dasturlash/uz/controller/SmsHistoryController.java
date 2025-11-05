package dasturlash.uz.controller;

import dasturlash.uz.dto.SmsHistoryDTO;
import dasturlash.uz.dto.auth.JwtDTO;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.exp.AppAccessDeniedException;
import dasturlash.uz.service.SmsHistoryService;
import dasturlash.uz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsHistoryController {
    @Autowired
    private SmsHistoryService smsHistoryService;

    @GetMapping("/phone")
    public ResponseEntity<List<SmsHistoryDTO>> getByPhone(@RequestParam("phone") String  phone) {
        return ResponseEntity.ok(smsHistoryService.getSmsDtoByPhone(phone));
    }

    @GetMapping("/date")
    public ResponseEntity<List<SmsHistoryDTO>> getByDate(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(smsHistoryService.getSmsDtoByDate(date));
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<SmsHistoryDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "size", defaultValue = "5") int size,
                                                              @RequestHeader("Authorization") String token) {
        final String jwt = token.substring(7).trim();
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRoles().contains(ProfileRoleEnum.ROLE_ADMIN)) {
            throw new AppAccessDeniedException("Mazgi you do not have permission");
        }
        return ResponseEntity.ok(smsHistoryService.pagination(page, size));
    }
}