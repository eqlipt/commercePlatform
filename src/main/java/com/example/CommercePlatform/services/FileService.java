package com.example.CommercePlatform.services;

import com.example.CommercePlatform.models.Image;
import com.example.CommercePlatform.models.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    @Value("${upload.path}")
    private String uploadPath;

    public void upload(MultipartFile[] files, Product product) {
        if (files != null) {
            File uploadDir = new File(uploadPath);
            // Если данной дирректории по пути не сущетсвует
            if (!uploadDir.exists()) {
                // Создаем данную дирректорию
                uploadDir.mkdir();
            }

            Arrays.stream(files).toList().forEach(
                file -> {
                    if(!file.getOriginalFilename().equals("")) {
                        // Создаем уникальное имя файла
                        // UUID представляет неизменный универсальный уникальный идентификатор
                        String uuidFile = UUID.randomUUID().toString();
                        // file.getOriginalFilename() - наименование файла с формы
                        String resultFileName = uuidFile + "." + file.getOriginalFilename();
                        // Загружаем файл по указаннопу пути
                        try {
                            file.transferTo(new File(uploadPath + "/" + resultFileName));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Image image = new Image();
                        image.setProduct(product);
                        image.setFileName(resultFileName);
                        product.addImageToProduct(image);
                    }
                }
            );
        }
    }

    public void deleteImage(String imageFileName) {
        var fileToDelete = new File(uploadPath + "/" + imageFileName);
        if(fileToDelete.exists()) fileToDelete.delete();
    }

    public void deleteImages(List<String> images) {
        images.stream().forEach(img -> deleteImage(img));
    }
}
