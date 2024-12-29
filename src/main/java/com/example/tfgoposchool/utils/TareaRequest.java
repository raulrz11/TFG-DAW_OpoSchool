package com.example.tfgoposchool.utils;

import com.example.tfgoposchool.dtos.tareas.TareaCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TareaRequest {
    private TareaCreateDto dto;
    private MultipartFile archivo;
}
