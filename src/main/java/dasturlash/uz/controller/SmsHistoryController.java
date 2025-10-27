package dasturlash.uz.controller;

import dasturlash.uz.dto.SmsHistoryDTO;
import dasturlash.uz.service.SmsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                                              @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(smsHistoryService.pagination(page, size));
    }
}