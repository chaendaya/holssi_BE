package org.example.holssi_be.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.example.holssi_be.config.TwilioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;


@Service
public class WhatsAppService {

    @Autowired
    private TwilioConfig twilioConfig;

    @PostConstruct
    public void init() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    public void sendWhatsApp(String phone, String code) {
        String message = "Your verification code is: " + code;

        Message.creator(
                        new PhoneNumber("whatsapp:" + phone),
                        new PhoneNumber("whatsapp:" + twilioConfig.getFromNumber()),
                        message)
                .create();
    }
}
