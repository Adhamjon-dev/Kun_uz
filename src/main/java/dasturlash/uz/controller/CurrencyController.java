package dasturlash.uz.controller;

import dasturlash.uz.dto.CurrencyDTO;
import dasturlash.uz.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/get/{ccy}")
    public ResponseEntity<CurrencyDTO> get(@PathVariable String ccy) {
        return ResponseEntity.ok(currencyService.getByCcy(ccy));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CurrencyDTO>> all() {
        return ResponseEntity.ok(currencyService.getAll());
    }
}
