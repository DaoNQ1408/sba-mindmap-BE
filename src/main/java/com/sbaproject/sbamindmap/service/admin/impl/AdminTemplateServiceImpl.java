package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.entity.Template;
import com.sbaproject.sbamindmap.repository.TemplateRepository;
import com.sbaproject.sbamindmap.service.admin.AdminTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTemplateServiceImpl implements AdminTemplateService {

    private final TemplateRepository templateRepository;

    @Override
    public List<Template> getAll() {
        return templateRepository.findAll();
    }

    @Override
    public Template getById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    @Override
    public Template create(Template template) {
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        return templateRepository.save(template);
    }

    @Override
    public Template update(Long id, Template request) {
        Template template = getById(id);
        template.setName(request.getName());
        template.setStyleConfig(request.getStyleConfig());
        template.setUpdatedAt(LocalDateTime.now());
        return templateRepository.save(template);
    }

    @Override
    public void delete(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new RuntimeException("Template not found");
        }
        templateRepository.deleteById(id);
    }
}
