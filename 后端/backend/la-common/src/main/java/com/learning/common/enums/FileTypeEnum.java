package com.learning.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源文件类型枚举
 */
@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    PDF("pdf", "PDF文档"),
    DOC("doc", "Word文档"),
    DOCX("docx", "Word文档"),
    PPT("ppt", "PowerPoint演示文稿"),
    PPTX("pptx", "PowerPoint演示文稿"),
    TXT("txt", "文本文件"),
    JPG("jpg", "JPEG图片"),
    JPEG("jpeg", "JPEG图片"),
    PNG("png", "PNG图片"),
    GIF("gif", "GIF图片"),
    ZIP("zip", "压缩文件"),
    RAR("rar", "压缩文件");

    private final String code;
    private final String desc;

    public static FileTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (FileTypeEnum fileTypeEnum : values()) {
            if (fileTypeEnum.getCode().equalsIgnoreCase(code)) {
                return fileTypeEnum;
            }
        }
        return null;
    }

    public static boolean isAllowed(String code) {
        FileTypeEnum[] allowed = {PDF, DOC, DOCX, PPT, PPTX, TXT};
        for (FileTypeEnum type : allowed) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }
}
