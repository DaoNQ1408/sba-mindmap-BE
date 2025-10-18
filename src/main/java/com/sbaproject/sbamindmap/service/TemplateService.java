package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.TemplateRequest;
import com.sbaproject.sbamindmap.dto.response.TemplateResponse;
import com.sbaproject.sbamindmap.entity.Template;

import java.util.List;

public interface TemplateService {
    Template findById(long templateId);
    TemplateResponse findResponseById(long templateId);
    List<TemplateResponse> findAllTemplates();
    TemplateResponse savedTemplate(TemplateRequest templateRequest);
    TemplateResponse updateTemplate(long templateId, TemplateRequest templateRequest);
    TemplateResponse deleteTemplate(long templateId);
}
