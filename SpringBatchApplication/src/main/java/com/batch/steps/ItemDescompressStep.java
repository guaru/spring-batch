package com.batch.steps;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@AllArgsConstructor
public class ItemDescompressStep implements Tasklet {

    private final ResourceLoader resourceLoader;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("------------------> Inicio del paso de decrompresion <------------------");
        Resource resource =  resourceLoader.getResource("classpath:files/persons.zip");
        String filePath = resource.getFile().getAbsolutePath();
        ZipFile zipFile = new ZipFile(filePath);
        File destDir =  new File(resource.getFile().getParent());
        if(!destDir.exists()) { destDir.mkdir(); }
        Enumeration<? extends ZipEntry> entries =  zipFile.entries();
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File entryDestination = new File(destDir, entry.getName());
            if(entry.isDirectory()) {
                entryDestination.mkdirs();
            } else {
                entryDestination.getParentFile().mkdirs();
                InputStream inputSteam = zipFile.getInputStream(entry);
                FileOutputStream outputSteam = new FileOutputStream(entryDestination);
                byte[] buffer =  new byte[1024];
                int length;
                while((length = inputSteam.read()) > 0) {
                    outputSteam.write(buffer,0, length);
                }
                outputSteam.close();
                inputSteam.close();
            }
        }
        zipFile.close();
        log.info("------------------> Fin del paso de decrompresion <------------------");
        return RepeatStatus.FINISHED;
    }

}
