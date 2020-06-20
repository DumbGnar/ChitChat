package com.example.demo;

import com.example.demo.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@SpringBootTest
public class ChitChatApplicationTests {

    private String encodeImage(String path) throws IOException {
        Path file = Paths.get(path);
        byte[] imageBytes = Files.readAllBytes(file);
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    @Test
    void testSaveBase64Image() {
        try {
            String encodedBase64Image = encodeImage("/Users/hzy/Desktop/spring.jpg");
            ImageService.saveBase64Image(encodedBase64Image, "/Users/hzy/Desktop/spring-2.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
