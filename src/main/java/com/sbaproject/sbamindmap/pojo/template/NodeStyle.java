package com.sbaproject.sbamindmap.pojo.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeStyle implements Serializable {
    private String backgroundColor;
    private String color;
    private String border;
    private Integer borderRadius;
    private Integer fontSize;
    private Integer padding;
}
