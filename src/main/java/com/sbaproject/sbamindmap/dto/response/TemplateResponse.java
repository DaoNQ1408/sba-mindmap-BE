package com.sbaproject.sbamindmap.dto.response;

import com.sbaproject.sbamindmap.pojo.template.StyleConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateResponse {
    private Long id;
    private String name;
    private StyleConfig styleConfig;
    private LocalDateTime creatAt;
    private LocalDateTime updateAt;
}
