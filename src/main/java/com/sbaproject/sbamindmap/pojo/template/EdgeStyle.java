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
public class EdgeStyle implements Serializable {
    private String stroke;
    private Integer strokeWidth;
    private Boolean animated;
    private String type;
}
