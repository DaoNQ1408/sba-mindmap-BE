package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.TemplateRequest;
import com.sbaproject.sbamindmap.dto.response.TemplateResponse;
import com.sbaproject.sbamindmap.entity.Template;
import com.sbaproject.sbamindmap.mapper.TemplateMapper;
import com.sbaproject.sbamindmap.repository.TemplateRepository;
import com.sbaproject.sbamindmap.service.TemplateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final TemplateMapper templateMapper;


    @Override
    public Template findById(long templateId) {
        return templateRepository.findById(templateId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Template not found with id: " +
                                        templateId)
                );
    }


    @Override
    public TemplateResponse findResponseById(long templateId) {

        Template template = findById(templateId);

        return templateMapper.toResponse(template);
    }


    @Override
    public List<TemplateResponse> findAllTemplates() {
        return templateRepository.findAll().stream()
                .map(templateMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public TemplateResponse savedTemplate(TemplateRequest templateRequest) {

        validateTemplateNameUniqueness(templateRequest.getName());

        Template template = templateMapper.toEntity(templateRequest);

        Template savedTemplate = templateRepository.save(template);

        return templateMapper.toResponse(savedTemplate);
    }


    @Override
    @Transactional
    public TemplateResponse updateTemplate(long templateId, TemplateRequest templateRequest) {

        Template template = findById(templateId);

        templateMapper.updateEntityFromRequest(template, templateRequest);

        Template updatedTemplate = templateRepository.save(template);

        return templateMapper.toResponse(updatedTemplate);
    }


    @Override
    @Transactional
    public TemplateResponse deleteTemplate(long templateId) {

        Template template = findById(templateId);

        templateRepository.delete(template);

        return templateMapper.toResponse(template);
    }


    public void validateTemplateNameUniqueness(String name) {
        templateRepository.findByName(name)
                .ifPresent(template -> {
                    throw new IllegalArgumentException(
                            "Template with name '" +
                                    name +
                                    "' already exists.");
                });
    }
}
