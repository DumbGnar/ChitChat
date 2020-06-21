package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageService {

    public static void saveBase64Image(String base64Image, String path) throws IOException, IllegalArgumentException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        Path file = Paths.get(path);
        Files.write(file, decodedBytes);
    }
}
