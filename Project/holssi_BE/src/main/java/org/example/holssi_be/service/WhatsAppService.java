package org.example.holssi_be.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.config.TwilioConfig;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final TwilioConfig twilioConfig;

    @PostConstruct
    public void init() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }
    public void sendWhatsApp(String phone, String code) {
        String from = twilioConfig.getFromNumber();
        String to = phone;
        String templateSid = twilioConfig.getTemplateSid(); // 템플릿 SID를 설정 파일에서 가져옴

        // JSON 형식의 문자열로 템플릿 변수 설정
        String templateParams = String.format("{\"1\":\"%s\"}", code);

        Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("whatsapp:" + to),
                            new com.twilio.type.PhoneNumber("whatsapp:" + to),  // MGXXXX.. 서비스SID
                            templateSid)
                .setContentVariables(templateParams)
                .create();

        System.out.println(message.getBody());
    }
}