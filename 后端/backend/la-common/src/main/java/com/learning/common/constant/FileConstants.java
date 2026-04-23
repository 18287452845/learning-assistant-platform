package com.learning.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 允许的文件类型列表
 */
public class FileConstants {

    /**
     * 允许的文件类型
     */
    public static final List<String> ALLOWED_TYPES = Arrays.asList(
        "pdf", "doc", "docx", "ppt", "pptx", "txt"
    );

    /**
     * 图片类型
     */
    public static final List<String> IMAGE_TYPES = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    /**
     * 文档类型
     */
    public static final List<String> DOCUMENT_TYPES = Arrays.asList(
        "pdf", "doc", "docx", "txt", "xls", "xlsx"
    );

    /**
     * 演示文稿类型
     */
    public static final List<String> PRESENTATION_TYPES = Arrays.asList(
        "ppt", "pptx"
    );

    /**
     * 压缩文件类型
     */
    public static final List<String> ARCHIVE_TYPES = Arrays.asList(
        "zip", "rar", "7z", "tar", "gz"
    );
}
