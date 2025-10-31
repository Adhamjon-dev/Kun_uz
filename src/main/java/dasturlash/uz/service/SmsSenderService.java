package dasturlash.uz.service;

import dasturlash.uz.dto.sms.SmsProviderTokenDTO;
import dasturlash.uz.dto.sms.SmsRequestDTO;
import dasturlash.uz.dto.sms.SmsTokenProviderResponse;
import dasturlash.uz.entitiy.SmsTokenEntity;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.SmsTokenRepository;
import dasturlash.uz.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class SmsSenderService {
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsTokenRepository smsTokenRepository;

    @Value("${sms.eskiz.email}")
    private String smsLogin;
    @Value("${sms.eskiz.password}")
    private String smsPassword;

    private static final String loginURL = "https://notify.eskiz.uz/api/auth/login";
    private static final String smsSendURL = "https://notify.eskiz.uz/api/message/sms/send";

    public void sendRegistrationSMS(String phone) {
        Integer smsCode = RandomUtil.fiveDigit();
        String body = "Bu Eskiz dan test"; // test message
        // ...
        System.out.println(body);
        smsHistoryService.save(phone, body, String.valueOf(smsCode));
        // ...
        send(phone, body);
    }


    public void send(String phone, String text) {
        SmsRequestDTO body = new SmsRequestDTO();
        body.setMobile_phone(phone);
        body.setMessage(text);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + getToken());

        RequestEntity<SmsRequestDTO> request = RequestEntity
                .post(smsSendURL)
                .headers(headers)
                .body(body);

        try {
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            // Masalan: noto‘g‘ri token yoki 401 Unauthorized
            throw new AppBadException("exception: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }


    private String getToken() {
        Optional<SmsTokenEntity> optionalToken = smsTokenRepository.getTopByOrderByCreatedDateDesc();

        if (optionalToken.isPresent()) {
            SmsTokenEntity tokenEntity = optionalToken.get();
            long days = ChronoUnit.DAYS.between(tokenEntity.getCreatedDate(), LocalDateTime.now());
            if (days < 30) {
                // Token hali yaroqli
                return tokenEntity.getToken();
            }
        }

        // Token yo‘q yoki muddati o‘tgan — yangisini olish
        String newToken = requestNewToken();

        if (newToken != null) {
            SmsTokenEntity entity = optionalToken.orElse(new SmsTokenEntity());
            entity.setToken(newToken);
            smsTokenRepository.save(entity);
        }
        return newToken;
    }

    private String requestNewToken() {
        SmsProviderTokenDTO body = new SmsProviderTokenDTO();
        body.setEmail(smsLogin);
        body.setPassword(smsPassword);

        RequestEntity<SmsProviderTokenDTO> request = RequestEntity
                .post(loginURL)
                .body(body);

        try {
            ResponseEntity<SmsTokenProviderResponse> response = restTemplate.exchange(
                    request,
                    SmsTokenProviderResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()
                    && response.getBody() != null
                    && response.getBody().getData() != null) {
                return response.getBody().getData().getToken();
            }

        } catch (HttpClientErrorException e) {
            throw new AppBadException("exception: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        return null;
    }
}
