package com.javahunter.scm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.javahunter.scm.helper.AppConstants;
import com.javahunter.scm.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;


    @Override
    public String uploadImage(MultipartFile contactImage, String fileName) {
        try {
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
//            String fileName = UUID.randomUUID().toString();
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id",fileName
            ));
            return this.getUrlFromPublicId(fileName);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.info("Exception thrown");
            return null;

        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary
                .url()
                .transformation(new Transformation<>()
                        .width(AppConstants.CONTACT_IMAGE_WIDTH)
                        .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                        .crop(AppConstants.CONTACT_IMAGE_CROP)
                ).generate(publicId);
    }


}
