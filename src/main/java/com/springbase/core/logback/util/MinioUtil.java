package com.springbase.core.logback.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbase.core.common.define.CoreErrCode;
import com.springbase.core.exception.CoreException;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springbase.core.logback.properties.MinioProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MinioUtil {

    private final MinioProperties minioInfo;

    private MinioClient minioClient;


    public MinioUtil(MinioProperties minio) {
        this.minioInfo = minio;
        if (!minioInfo.isEnable()) return;
        log.trace("Create minioClient [{}]", minio);
        minioClient = MinioClient.builder()
                .endpoint(minioInfo.getMinioUrl())
                .credentials(minioInfo.getAccessKey(), minioInfo.getSecretKey())
                .build()
        ;
    }

    public boolean minioCheckFile(String bucket, String filePath, String fileNm, String suffix) throws CoreException
    {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(filePath + fileNm + suffix)
                            .build()
            );

            // If statObject did NOT throw exception → file exists
            return true;
        }catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException ex)
        {
            return false;
        }

    }

    public void minioUploadFile(String uploadPath, InputStream file) throws CoreException {
        minioUploadFile(minioInfo.getBucketName(), uploadPath, file);
    }

    public void minioUploadFile(String bucketName, String uploadPath, InputStream file) throws CoreException {
        if (!minioInfo.isEnable()) {
            throw new CoreException(CoreErrCode.FILE_SYSTEM_NOT_REACH);
        }
        try {

            // Make 'asiatrip' bucket if not exist.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                log.info("Bucket {} already exists.", minioInfo.getBucketName());
            }

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName)
                            .object(uploadPath)
                            .stream(file, -1, 10485760)
                            .build())
            ;
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException ex) {
            throw new CoreException(CoreErrCode.FILE_UPLOAD_FAIL);
        }

    }

    public File minioDownLoadFile(String filePath, String fileNm, String suffix, File downloadPath) throws CoreException {
        return minioDownLoadFile(minioInfo.getBucketName(), filePath, fileNm, suffix, downloadPath);
    }

    public File minioDownLoadFile(String bucket, String filePath, String fileNm, String suffix, File downloadPath) throws CoreException {

        if (!minioInfo.isEnable()) {
            throw new CoreException(CoreErrCode.FILE_SYSTEM_NOT_REACH);
        }

        File tempFile = new File(downloadPath, fileNm + suffix);
        tempFile.deleteOnExit();

        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(filePath + fileNm + suffix)
                        .build()
        )) {
            FileUtils.copyInputStreamToFile(stream, tempFile);
        } catch (Exception e) {
            throw new CoreException(CoreErrCode.FILE_NOT_FOUND);
        }

        return tempFile;
    }


    public <T> List<T> minioReadFiles(String bucket, String filePath, String fileNm, String suffix,
                                      Class<T> clazz) throws CoreException {

        if (!minioInfo.isEnable()) {
            throw new CoreException(CoreErrCode.FILE_SYSTEM_NOT_REACH);
        }
        InputStream stream = null;


        log.debug("find in minio , filePath = [{}], fileNm = [{}], suffix = [{}]", filePath, fileNm, suffix);

        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filePath + fileNm + suffix)
                    .build()
            );
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    stream,
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            throw new CoreException(CoreErrCode.FILE_NOT_FOUND);
        }
    }

    public void minioWriteFiles(String bucket, String filePath, String fileNm, String suffix, Object data) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);

        ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));


        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(filePath + fileNm + suffix)
                .stream(bais, -1, 10485760)
                .contentType("application/json")
                .build());

        bais.close();
    }

    public void minioDeleteFile(String bucket, String filePath, String fileNm, String suffix) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(filePath + fileNm + suffix)
                        .recursive(true)
                        .includeVersions(true)
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(item.objectName())
                            .versionId(item.versionId())
                            .build()
            );
        }
    }


    public <T> Map<String, List<T>> getAllFileInBucketAsJson(
            String bucket,
            String pathway,
            Class<T> clazz
    ) throws CoreException {

        Map<String, List<T>> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(pathway)
                        .recursive(true)
                        .build()
        );

        try {
            for (Result<Item> itemResult : items) {
                Item item = itemResult.get();
                String objectName = item.objectName();

                if (objectName.endsWith(".json") && !objectName.equals("version.json")) {

                    InputStream is = minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(objectName)
                                    .build()
                    );

                    // ⭐ 동적 타입으로 JSON → List<T> 변환
                    List<T> codeList = mapper.readValue(
                            is,
                            mapper.getTypeFactory()
                                    .constructCollectionType(List.class, clazz)
                    );

                    // 파일명 → C101 등 codeId 추출
                    String codeId = objectName.substring(
                            pathway.length(),
                            objectName.length() - 5
                    );

                    result.put(codeId, codeList);

                    is.close();
                }
            }
        } catch (Exception e) {
            throw new CoreException(CoreErrCode.FILE_NOT_FOUND);
        }

        return result;
    }
}
