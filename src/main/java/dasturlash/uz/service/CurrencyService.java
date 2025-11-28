package dasturlash.uz.service;

import dasturlash.uz.dto.CurrencyDTO;
import dasturlash.uz.exp.AppBadException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CurrencyService {
    @Autowired
    private  RestTemplate restTemplate;

    private final Map<String, CurrencyDTO> cache = new ConcurrentHashMap<>();

    @PostConstruct
    @Scheduled(cron = "0 0 9 * * ?")
    public void updateDailyRates() {
        String today = LocalDate.now().toString();
        String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/all/" + today + "/";

        try {
            CurrencyDTO[] response = restTemplate.getForObject(url, CurrencyDTO[].class);

            if (response != null) {
                log.info("refresh currency response");
                saveAll(List.of(response));
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Not fount url:  {}", url);
            throw new AppBadException(e.getMessage());
        }
    }

    public void saveAll(List<CurrencyDTO> list) {
        cache.clear();
        list.forEach(dto -> cache.put(dto.getCcy(), dto));
    }

    public CurrencyDTO getByCcy(String ccy) {
        return cache.get(ccy);
    }

    public List<CurrencyDTO> getAll() {
        List<CurrencyDTO> list = new LinkedList<>();
        cache.forEach((key, value) -> list.add(value));
        return list;
    }
}
