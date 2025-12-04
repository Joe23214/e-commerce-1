package com.company.ecomShop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(FirebaseInitializer.class);

    @Value("${firebase.service.account.path}")
    private String serviceAccountPath;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount;

            if (serviceAccountPath.startsWith("classpath:")) {
                String path = serviceAccountPath.replace("classpath:", "");
                serviceAccount = getClass().getClassLoader().getResourceAsStream(path);
            } else {
                serviceAccount = new java.io.FileInputStream(serviceAccountPath);
            }

            if (serviceAccount == null) {
                log.error("❌ File di servizio Firebase non trovato: {}", serviceAccountPath);
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("✅ Firebase inizializzato con successo!");
            }

        } catch (Exception e) {
            log.error("❌ Errore durante l'inizializzazione di Firebase", e);
        }
    }
}
