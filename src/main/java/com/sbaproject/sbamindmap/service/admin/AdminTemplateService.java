package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.Template;

import java.util.List;

public interface AdminTemplateService {
    List<Template> getAll();
    Template getById(Long id);
    Template create(Template template);
    Template update(Long id, Template template);
    void delete(Long id);
}
