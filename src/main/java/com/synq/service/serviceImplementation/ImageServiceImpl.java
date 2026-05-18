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

        if (contactImage == null || contactImage.isEmpty()) {
            log.info("No file uploaded or file is empty. Skipping Cloudinary upload.");
            return null;
        }

        String fileName = UUID.randomUUID().toString();
        try {
            byte[] data = contactImage.getBytes();

            log.info("Uploading binary payload to Cloudinary with Public ID: {}", fileName);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", fileName
            ));

            log.info("Fetching transformed asset public CDN secure URL path loop...");
            return this.getUrlFromPublicId(fileName);

        } catch (IOException e) {
            log.error("Cloudinary upload failed due to systemic transport exception: ", e);
            throw new RuntimeException("Failed to store file on cloud architecture provider: " + e.getMessage());
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
                .generate(publicId);
    }
}
