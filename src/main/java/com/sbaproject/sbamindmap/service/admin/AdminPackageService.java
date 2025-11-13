package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.Packages;

import java.util.List;

public interface AdminPackageService {
    List<Packages> getAll();
    Packages getById(Long id);
    Packages create(Packages pkg);
    Packages update(Long id, Packages request);
    void delete(Long id);
}
