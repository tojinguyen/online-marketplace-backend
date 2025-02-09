package com.learning.onlinemarketplace;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineMarketplaceApplication {

    public static void main(String[] args) {
        // Load file .env nếu tồn tại
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        // Đưa các biến trong .env vào System properties
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(OnlineMarketplaceApplication.class, args);
    }

}
