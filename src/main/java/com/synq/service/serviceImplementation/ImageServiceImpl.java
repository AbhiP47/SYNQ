package com.synq.service.serviceImplementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.synq.helpers.AppConstants;
import com.synq.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary)
    {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage) {

        // Using a unique ID is good practice
        String fileName = UUID.randomUUID().toString();

        try {
            // IMPROVEMENT: Use .getBytes() instead of available() for more reliability
            byte[] data = contactImage.getBytes();

            log.info("Image is being uploaded to Cloudinary...");

            // Grab the result map from the upload call
            Map uploadResult = cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", fileName
            ));

            // GET THE URL DIRECTLY FROM THE RESULT
            // "secure_url" is the HTTPS version
            String secureUrl = uploadResult.get("secure_url").toString();

            log.info("Upload Successful! URL: " + secureUrl);
            return secureUrl;

        } catch (IOException e) {
            log.error("Cloudinary Upload failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {

        return cloudinary
                .url()
                .transformation(
                        new Transformation()
                                .width(AppConstants.CONTACT_IMAGE_WIDTH)
                                .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                                .crop(AppConstants.CONTACT_IMAGE_CROP)
                )
                .generate();
    }
}
