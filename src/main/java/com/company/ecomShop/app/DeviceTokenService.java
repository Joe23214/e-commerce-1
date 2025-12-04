package com.company.ecomShop.app;

import com.company.ecomShop.entity.DeviceToken;
import io.jmix.core.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DeviceTokenService {

    private static final Logger log = LoggerFactory.getLogger(DeviceTokenService.class);

    private final DataManager dataManager;

    public DeviceTokenService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Transactional("marketnewstoredbTransactionManager")
    public DeviceToken registerOrUpdateToken(String userId, String token, String platform) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token vuoto");
        }
        Long userLong = null;
        try {
            userLong = Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            log.warn("UserId non numerico: {}", userId);
            throw ex;
        }

        DeviceToken existing = dataManager.load(DeviceToken.class)
                .query("select d from DeviceToken d where d.token = :token")
                .parameter("token", token)
                .optional()
                .orElse(null);

        if (existing == null) {
            DeviceToken dt = dataManager.create(DeviceToken.class);
            dt.setUser(userLong);
            dt.setToken(token);
            dt.setEnabled(true);
            dt.setPlatform(platform);
            dt.setLastSeen(new Date());
            DeviceToken saved = dataManager.save(dt);
            log.info("Registered new device token for user {}", userLong);
            return saved;
        } else {
            existing.setLastSeen(new Date());
            existing.setPlatform(platform);
            existing.setEnabled(true);
            DeviceToken saved = dataManager.save(existing);
            log.info("Updated device token for user {}", userLong);
            return saved;
        }
    }

    @Transactional("marketnewstoredbTransactionManager")
    public void disableToken(String token) {
        DeviceToken existing = dataManager.load(DeviceToken.class)
                .query("select d from DeviceToken d where d.token = :token")
                .parameter("token", token)
                .optional().orElse(null);
        if (existing != null) {
            existing.setEnabled(false);
            dataManager.save(existing);
            log.info("Disabled token {}", token);
        } else {
            log.warn("Tried to disable non-existing token {}", token);
        }
    }

    @Transactional("marketnewstoredbTransactionManager")
    public void updateLastSeen(String token, Date lastSeen) {


        DeviceToken existing = dataManager.load(DeviceToken.class)
                .query("select d from DeviceToken d where d.token = :token")
                .parameter("token", token)
                .optional()
                .orElse(null);

        if (existing != null) {
            existing.setLastSeen(lastSeen);
            existing.setEnabled(true);
            dataManager.save(existing);
            log.debug("Updated lastSeen for token {}", token);
        } else {
            log.warn("Ping received for unknown token {}", token);
        }
    }
}
