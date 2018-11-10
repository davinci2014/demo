package com.example.demo.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.example.demo.config.ApplicationProperties;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for sending sms.
 * <p>
 * We use the @Async annotation to send sms asynchronously.
 */
@Service
public class SmsService {

    private final ApplicationProperties.Aliyun.Sms sms;
    private final Logger log = LoggerFactory.getLogger(SmsService.class);

    public SmsService(ApplicationProperties applicationProperties) {
        this.sms = applicationProperties.getAliyun().getSms();
    }

    @Async
    public void sendRegisterSms(String phoneNumber, String nickname) {
        log.debug("Sending register sms to '{}' started.", phoneNumber);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nickname", nickname);

        sendSmsFromTemplate(phoneNumber, sms.getTemplates().getRegister(), jsonObject.toString());
    }

    @Async
    void sendSmsFromTemplate(String phoneNumber, String templateCode, String templateParam) {
        sendSms(phoneNumber, templateCode, templateParam, 0);
    }

    @Async
    void sendSms(String phoneNumber, String templateCode, String templateParam, int repeatTimes) {
        if (repeatTimes < 3) {
            try {
                // 设置超时时间
                System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
                System.setProperty("sun.net.client.defaultReadTimeout", "10000");

                IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", sms.getAccessKey(), sms.getAccessSecret());
                DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", sms.getProduct(), sms.getDomain());

                SendSmsRequest request = new SendSmsRequest();
                request.setMethod(MethodType.POST);
                request.setPhoneNumbers(phoneNumber);
                request.setSignName(sms.getSignName());
                request.setTemplateCode(templateCode);
                request.setTemplateParam(templateParam);

                IAcsClient acsClient = new DefaultAcsClient(profile);
                SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                    log.info("Send sms to '{}' succeed!", phoneNumber);
                } else {
                    sendSms(phoneNumber, templateCode, templateParam, ++repeatTimes);
                }
            } catch (Exception e) {
                log.info("Send sms to '{}' failed, repeatTimes = '{}': {}", phoneNumber, repeatTimes, e.getMessage());
            }
        }
    }
}
