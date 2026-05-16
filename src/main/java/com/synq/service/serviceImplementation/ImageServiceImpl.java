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

            log.info("Image is being uploaded at the cloudinary");
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", fileName
            ));

            log.info("Public URL is being fetched for the file : " + fileName);
            return this.getUrlFromPublicId(fileName);

        } catch (IOException e) {
            log.error("Cloudinary upload exception: " + e.getMessage());
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
