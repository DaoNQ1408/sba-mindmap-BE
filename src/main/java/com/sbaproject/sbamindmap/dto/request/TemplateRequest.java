package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.pojo.template.StyleConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {
    private String name;
    private StyleConfig styleConfig;
}
