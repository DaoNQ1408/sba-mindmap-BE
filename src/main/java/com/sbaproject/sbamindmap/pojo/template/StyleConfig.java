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
public class StyleConfig implements Serializable {
    private PaneStyle pane;
    private NodeStyle defaultNode;
    private EdgeStyle defaultEdge;
}
