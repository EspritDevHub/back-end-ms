package tn.esprit.pi.usermanagement.suermanagementmicroservice.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.services.UserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("/images")
public class ImageController {

    @Autowired
    UserService userService;
    @PostMapping("uploadImage")
    public ResponseEntity<?>  uploadImage(@RequestBody ImageRequest request) {
        try {
            // Decode Base64 string
            byte[] imageBytes = Base64.getDecoder().decode(request.getBase64Image());

            // Path to resources/images
            String path = new File("user-managment-ms\\src\\main\\resources\\images").getAbsolutePath();
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

           var user =  userService.getUserByEmail(request.email);
            if(user == null) {
                throw new RuntimeException("User not found");
            }
            File imageFile = new File(dir, user.getId()+".jpg");

            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imageBytes);
            }
            userService.activateUser(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "saved");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return null;
        }
    }

    @PostMapping("verify-faces")
    public ResponseEntity<String> callPythonApi(@RequestBody ImageRequest request) {
        try {
            String pythonApiUrl = "https://5de7-104-199-193-3.ngrok-free.app/verify_faces";

            // Load user and base path
            String path = new File("user-managment-ms/src/main/resources/images").getAbsolutePath();
            var user = userService.getUserByEmail(request.email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            // Load image1 (original saved image)
            File image1File = new File(path, user.getId() + ".jpg");
            if (!image1File.exists()) {
                return ResponseEntity.badRequest().body("Image1 not found for user: " + request.getUserId());
            }

            // Load image2 copy (already saved copy image)
            File image2File = new File(path, user.getId() + "copy.jpg");
            if (!image2File.exists()) {
                return ResponseEntity.badRequest().body("Image2 copy not found for user: " + request.getUserId());
            }

            // Prepare multipart request body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(MediaType.IMAGE_JPEG);

            HttpEntity<FileSystemResource> image1Entity = new HttpEntity<>(new FileSystemResource(image1File), fileHeaders);
            HttpEntity<FileSystemResource> image2Entity = new HttpEntity<>(new FileSystemResource(image2File), fileHeaders);

            body.add("image1", image1Entity);
            body.add("image2", image2Entity);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            System.out.println("Calling Python API...");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

            // Delete the copy image after sending
            if (image2File.exists()) {
                if (image2File.delete()) {
                    System.out.println("Copy image deleted successfully");
                } else {
                    System.out.println("Failed to delete copy image");
                }
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @PostMapping("uploadImageCopy")
    public ResponseEntity<String>  uploadImageCopy(@RequestBody ImageRequest request) {
        try {
            // Decode Base64 string
            byte[] imageBytes = Base64.getDecoder().decode(request.getBase64Image());

            // Path to resources/images
            String path = new File("user-managment-ms\\src\\main\\resources\\images").getAbsolutePath();
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            var user =  userService.getUserByEmail(request.email);
            if(user == null) {
                throw new RuntimeException("User not found");
            }
            File imageFile = new File(dir, user.getId()+"copy"+".jpg");

            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imageBytes);
            }

            return ResponseEntity.status(200).body("saved");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // DTO class
    @Getter
    @Setter
    public static class ImageRequest {
        private String base64Image;
        private String userId;
        private String email;
    }
}

