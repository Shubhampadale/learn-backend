package com.elearn.app.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class fileServiceImpl implements FileService {
    @Override
    public String save(MultipartFile file, String outputPath, String fileName) throws IOException {

        //get path from the String
        Path path =  Paths.get(outputPath);

       //create output folder if not exists
        Files.createDirectories(path);

        Path filePath =  Paths.get(path.toString(),file.getOriginalFilename());

        //file writes at given path
        Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }
}
