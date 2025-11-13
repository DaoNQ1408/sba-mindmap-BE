package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.entity.Packages;
import com.sbaproject.sbamindmap.repository.PackagesRepository;
import com.sbaproject.sbamindmap.service.admin.AdminPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPackageServiceImpl implements AdminPackageService {

    private final PackagesRepository packagesRepository;

    @Override
    public List<Packages> getAll() {
        return packagesRepository.findAll();
    }

    @Override
    public Packages getById(Long id) {
        return packagesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    @Override
    public Packages create(Packages pkg) {
        pkg.setCreatedAt(LocalDateTime.now());
        pkg.setUpdatedAt(LocalDateTime.now());
        return packagesRepository.save(pkg);
    }

    @Override
    public Packages update(Long id, Packages request) {
        Packages pkg = getById(id);
        pkg.setName(request.getName());
        pkg.setPrice(request.getPrice());
        pkg.setApiCallLimit(request.getApiCallLimit());
        pkg.setUpdatedAt(LocalDateTime.now());
        pkg.setDurationDays(request.getDurationDays());
        pkg.setCategory(request.getCategory());
        return packagesRepository.save(pkg);
    }

    @Override
    public void delete(Long id) {
        if (!packagesRepository.existsById(id)) throw new RuntimeException("Package not found");
        packagesRepository.deleteById(id);
    }
}
