package ru.kolpakovee.userservice.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${s3.api.bucket-name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String uploadFile(MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        UUID keyName = UUID.randomUUID();

        PutObjectRequest putRequest = new PutObjectRequest(bucketName, keyName.toString(), file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(putRequest);

        return keyName.toString();
    }

    public String uploadBase64File(String base64String) {
        byte[] fileBytes = Base64.getDecoder().decode(base64String);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);

        UUID keyName = UUID.randomUUID();

        String keyNameStr = keyName + ".jpg";

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, keyNameStr, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            s3Client.putObject(putRequest);

            return "https://" + bucketName + ".hb.ru-msk.vkcs.cloud/" + keyNameStr;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload base64 file to S3", e);
        }
    }

    public String getBase64File(String key) {
        S3Object s3Object = s3Client.getObject(bucketName, key);
        try (InputStream inputStream = s3Object.getObjectContent()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file from S3", e);
        }
    }
}
