package com.company.ecomShop.app;

import com.company.ecomShop.entity.DeviceToken;
import io.jmix.core.DataManager;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FcmService {

    private static final Logger log = LoggerFactory.getLogger(FcmService.class);
    private final DataManager dataManager;
    private final DeviceTokenService deviceTokenService;

    public FcmService(DataManager dataManager, DeviceTokenService deviceTokenService) {
        this.dataManager = dataManager;
        this.deviceTokenService = deviceTokenService;
    }

    public void sendNotificationToUser(Long userId, String title, String body, Map<String, String> data) {
        List<DeviceToken> tokens = dataManager.load(DeviceToken.class)
                .query("select d from DeviceToken d where d.user = :userId and d.enabled = true")
                .parameter("userId", userId)
                .list();

        if (tokens.isEmpty()) {
            log.info("No tokens found for user {}", userId);
            return;
        }

        List<String> tokenList = tokens.stream().map(DeviceToken::getToken).collect(Collectors.toList());
        if (tokenList.isEmpty()) return;

        MulticastMessage message = MulticastMessage.builder()
                .putAllData(data == null ? Collections.emptyMap() : data)
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .addAllTokens(tokenList)
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            log.info("FCM send results: success={}, failure={}", response.getSuccessCount(), response.getFailureCount());

            for (int i = 0; i < response.getResponses().size(); i++) {
                SendResponse r = response.getResponses().get(i);
                if (!r.isSuccessful()) {
                    String failedToken = tokenList.get(i);
                    String error = r.getException() != null ? r.getException().getMessage() : "Unknown error";
                    log.warn("Failed token: {} -> {}", failedToken, error);

                    // If token is invalid or unregistered, disable it to avoid future attempts
                    // check for known error strings like "registration-token-not-registered"
                    deviceTokenService.disableToken(failedToken);
                }
            }
        } catch (FirebaseMessagingException e) {
            log.error("FirebaseMessagingException while sending notifications", e);
        } catch (Exception e) {
            log.error("Unexpected error while sending FCM messages", e);
        }
    }
}
