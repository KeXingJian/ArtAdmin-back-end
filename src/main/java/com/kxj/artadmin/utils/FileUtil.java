package com.kxj.artadmin.utils;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {

    public static String uploadPicture(MultipartFile file, String rootPath) {
        // 生成新的文件名
        String originalFilename = file.getOriginalFilename();
        String newFilename = IdUtil.fastSimpleUUID() + getExtension(originalFilename);

        // 目标文件路径
        Path targetLocation = Paths.get(rootPath).resolve(newFilename);

        // 确保目标目录存在
        try {
            Files.createDirectories(targetLocation.getParent());
            // 将文件保存到目标位置
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + originalFilename + ". Please try again!", e);
        }
        return newFilename;
    }

    private static String getExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastIndexOfDot = filename.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // No extension
        }
        return filename.substring(lastIndexOfDot).toLowerCase();
    }

    public static void removePicture(String sourceFile, String target) throws IOException {
        // 源文件路径
        Path sourcePath = Paths.get(sourceFile);

        // 目标目录路径
        Path targetDir = Paths.get(target);

        // 目标文件路径
        Path targetPath = targetDir.resolve(sourcePath.getFileName());


            // 确保目标目录存在
            Files.createDirectories(targetDir);

            // 迁移文件
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("文件已成功迁移至: " + targetPath);



    }

    public static List<String> moveVideoToAudit(String sourceDirPath, String targetDirPath) throws IOException {
        // 创建源文件夹和目标文件夹的 Path 对象
        Path sourceDir = Paths.get(sourceDirPath);
        Path targetDir = Paths.get(targetDirPath);
        List<String> uuidList = new ArrayList<>();

        // 检查源文件夹是否存在
        if (!Files.exists(sourceDir)) {
            throw new IllegalArgumentException("Source directory does not exist: " + sourceDirPath);
        }

        // 检查目标文件夹是否存在，如果不存在则创建
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }


        // 获取源文件夹下的所有文件（不包括子文件夹）
        try (Stream<Path> walk = Files.walk(sourceDir, 1)) {

            walk.filter(Files::isRegularFile)
                    .toList().forEach(path -> {

                        String fileName = path.getFileName().toString();
                        String newFileName = IdUtil.fastSimpleUUID() + getExtension(fileName);
                        uuidList.add(newFileName);
                        Path targetFile = targetDir.resolve(newFileName);

                        try {
                            Files.move(path, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });


           return uuidList;

        }
    }

    public static void moveAuditToPass(String sourcePath, String targetPath, String filename) throws Exception {
        // 分割文件名
        String[] parts = filename.split("\\$");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid filename format. Expected format: 'subdir$filename'");
        }

        // 构造完整的源文件路径
        Path src = Paths.get(sourcePath, parts[1]);

        // 构造完整的目标文件路径
        Path destDir = Paths.get(targetPath, parts[0]);
        Path destFile = destDir.resolve(parts[1]);

        // 如果目标文件夹不存在，则创建它
        Files.createDirectories(destDir);

        // 移动文件到新的位置
        Files.move(src, destFile, StandardCopyOption.REPLACE_EXISTING);
    }

}
