package dasturlash.uz.service;

import dasturlash.uz.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderService {
    @Autowired
    private SmsHistoryService smsHistoryService;

    public void sendRegistrationSMS(String phone) {
        Integer smsCode = RandomUtil.fiveDigit();
        String body = "Kun.uz partalidan ro'yhatdan otish uchun tastiqlash qodi: " + smsCode; // test message
        // ...
        System.out.println(body);
        smsHistoryService.save(phone, body, String.valueOf(smsCode));
        // ...
    }

}
