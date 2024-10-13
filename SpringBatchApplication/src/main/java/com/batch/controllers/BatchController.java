package com.batch.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class BatchController {

    private final  JobLauncher jobLauncher;
    private final Job job;

    @PostMapping("/upload-file")
    public ResponseEntity<?> receiveFile(@RequestParam(name = "file")MultipartFile multipartFile) {
        String fileName =  multipartFile.getOriginalFilename();
        try {
            Path path = Paths.get("src"+ File.separator+ "main"+ File.separator + "resources"+ File.separator + "files"+ File.separator + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            Map<String, String> response = Map.of("archivo", fileName, "status", "Recibido");

            JobParameters jobParameter = new JobParametersBuilder().addDate("fecha", new Date())
                    .addString("fileName", fileName).toJobParameters();
            jobLauncher.run(job, jobParameter);
            return ResponseEntity.ok(response);
        } catch(Exception e) {
            log.error("Error al iniciar el proceso batch", e.getMessage());
            throw  new RuntimeException();
        }
    }
}
