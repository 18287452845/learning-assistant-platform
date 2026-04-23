package com.learning.service;

import cn.hutool.core.io.FileUtil;
import com.learning.common.constant.Constants;
import com.learning.common.exception.BusinessException;
import com.learning.infra.config.UploadProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final UploadProperties uploadProperties;

    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile file, String subDir) {
        // 验证文件
        validateFile(file);

        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);
        
        // 生成新文件名
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        
        // 创建目录
        String uploadPath = uploadProperties.getBasePath() + File.separator + subDir;
        Path dirPath = Paths.get(uploadPath);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                throw BusinessException.fail("创建上传目录失败");
            }
        }
        
        // 保存文件
        Path filePath = dirPath.resolve(newFileName);
        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw BusinessException.fail("文件上传失败");
        }
        
        log.info("文件上传成功: originalFilename={}, newFileName={}, path={}", 
            originalFilename, newFileName, filePath);
        
        return "/" + subDir + "/" + newFileName;
    }

    /**
     * 上传头像
     */
    public String uploadAvatar(MultipartFile file) {
        // 验证文件类型
        String extension = FileUtil.extName(file.getOriginalFilename());
        if (!extension.equalsIgnoreCase("jpg") && 
            !extension.equalsIgnoreCase("jpeg") && 
            !extension.equalsIgnoreCase("png") && 
            !extension.equalsIgnoreCase("gif")) {
            throw BusinessException.paramError("头像只支持jpg、jpeg、png、gif格式");
        }
        
        // 验证文件大小（最大2MB）
        if (file.getSize() > 2 * 1024 * 1024) {
            throw BusinessException.paramError("头像大小不能超过2MB");
        }
        
        return uploadFile(file, "avatar");
    }

    /**
     * 上传资源文件
     */
    public String uploadResource(MultipartFile file) {
        return uploadFile(file, "resources");
    }

    /**
     * 删除文件
     */
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        
        Path path = Paths.get(uploadProperties.getBasePath() + filePath);
        try {
            Files.deleteIfExists(path);
            log.info("文件删除成功: path={}", path);
        } catch (IOException e) {
            log.warn("文件删除失败: path={}", path, e);
        }
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw BusinessException.paramError("文件不能为空");
        }
        
        // 验证文件大小
        if (file.getSize() > Constants.File.MAX_SIZE) {
            throw BusinessException.paramError("文件大小不能超过50MB");
        }
        
        // 验证文件类型
        String extension = FileUtil.extName(file.getOriginalFilename());
        if (!Constants.File.ALLOWED_TYPESList.contains(extension.toLowerCase())) {
            throw BusinessException.paramError("不支持的文件类型");
        }
    }
}
