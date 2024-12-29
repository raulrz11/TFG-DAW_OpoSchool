package com.example.tfgoposchool.config.storage;

import com.example.tfgoposchool.storage.service.FileSystemStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    private final FileSystemStorageService storageService;

    @Value("${upload.delete}")
    private String deleteAll;

    @Autowired
    public StorageConfig(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            storageService.deleteAll();
        }

        storageService.init();
    }
}
