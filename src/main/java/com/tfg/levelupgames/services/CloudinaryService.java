package com.tfg.levelupgames.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    public CloudinaryService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dono6abrn",
            "api_key", "292832137389456",
            "api_secret", "bQr6UFPIvN29bnc4FRy2x64rbGQ"));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> upload(MultipartFile file) throws IOException {
        return (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> delete(String publicId) throws IOException {
        return (Map<String, Object>) cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}