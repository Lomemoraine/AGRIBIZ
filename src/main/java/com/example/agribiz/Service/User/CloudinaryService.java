package com.example.agribiz.Service.User;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private Cloudinary cloudinary;

    private Cloudinary getCloudinary() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret));
        }
        return cloudinary;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        log.info("Uploading image to Cloudinary: {}", file.getOriginalFilename());

        validateFile(file);

        // Create transformation using the Transformation class
        Transformation transformation = new Transformation()
                .width(400)
                .height(400)
                .crop("fill")
                .gravity("face");

        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "resource_type", "image",
                "folder", "potato-platform/profiles",
                "transformation", transformation
        );

        Map uploadResult = getCloudinary().uploader().upload(file.getBytes(), uploadOptions);
        String imageUrl = (String) uploadResult.get("secure_url");

        log.info("Image uploaded successfully to Cloudinary: {}", imageUrl);
        return imageUrl;
    }

    public void deleteImage(String publicId) throws IOException {
        log.info("Deleting image from Cloudinary: {}", publicId);

        Map deleteResult = getCloudinary().uploader().destroy(publicId, ObjectUtils.emptyMap());

        log.info("Image deletion result: {}", deleteResult.get("result"));
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Check file size (5MB limit)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        // Check allowed image types
        if (!isAllowedImageType(contentType)) {
            throw new IllegalArgumentException("Only JPEG, PNG, and GIF images are allowed");
        }
    }

    private boolean isAllowedImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/jpg");
    }
}