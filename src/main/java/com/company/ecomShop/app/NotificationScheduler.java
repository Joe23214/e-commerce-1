package com.company.ecomShop.app;

import com.company.ecomShop.entity.DeviceToken;
import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class NotificationScheduler {

    private final DataManager dataManager;
    private final FcmService fcmService;
    private final SystemAuthenticator systemAuthenticator;

    public NotificationScheduler(DataManager dataManager, FcmService fcmService, SystemAuthenticator systemAuthenticator) {
        this.dataManager = dataManager;
        this.fcmService = fcmService;
        this.systemAuthenticator = systemAuthenticator;
    }

    // ✅ metodo NON statico
    public void sendWelcomeNotification(Long userId) {
        systemAuthenticator.withSystem(() -> {
            DeviceToken token = dataManager.load(DeviceToken.class)
                    .query("select d from DeviceToken d where d.enabled = true and d.user = :userId")
                    .parameter("userId", userId)
                    .optional()
                    .orElse(null);

            if (token != null) {
                fcmService.sendNotificationToUser(
                        token.getUser(),
                        "Benvenuto!",
                        "Grazie per aver effettuato l'accesso in app, vi auguriamo una buona permanenza!",
                        Map.of(
                                "icon", "ic_notification",
                                "sound", "default"
                        )
                );
            }
            return null;
        });
    }

    // 2️⃣ Notifica "Ci manchi" ogni 12 ore
    @Scheduled(fixedRate = 12 * 60 * 60 * 1000) // ogni 12 ore
    public void sendMissYouNotifications() {
        systemAuthenticator.withSystem(() -> {
            Date twelveHoursAgo = new Date(System.currentTimeMillis() - 12 * 60 * 60 * 1000);

            List<DeviceToken> tokens = dataManager.load(DeviceToken.class)
                    .query("select d from DeviceToken d where d.enabled = true and d.lastSeen < :time")
                    .parameter("time", twelveHoursAgo)
                    .list();

            for (DeviceToken dt : tokens) {
                fcmService.sendNotificationToUser(
                        dt.getUser(),
                        "Ci manchi!",
                        "Torna a fare acquisti da noi!",
                        Map.of(
                                "icon", "ic_notification",
                                "sound", "default"
                        )
                );
            }

            return null;
        });
    }

    // 3️⃣ Notifica ricorrente una volta al giorno
    @Scheduled(cron = "0 0 12 * * ?") // ogni giorno a mezzogiorno
    public void sendRecurringPromotions() {
        systemAuthenticator.withSystem(() -> {
            List<DeviceToken> tokens = dataManager.load(DeviceToken.class)
                    .query("select d from DeviceToken d where d.enabled = true")
                    .list();

            for (DeviceToken dt : tokens) {
                fcmService.sendNotificationToUser(
                        dt.getUser(),
                        "Promozione speciale!",
                        "Non perdere le ultime offerte!",
                        Map.of(
                                "icon", "ic_notification",
                                "sound", "default"
                        )
                );
            }

            return null;
        });
    }
}
