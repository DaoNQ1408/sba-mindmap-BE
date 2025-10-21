package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.PackageRequest;
import com.sbaproject.sbamindmap.entity.Packages;
import com.sbaproject.sbamindmap.entity.Template;
import com.sbaproject.sbamindmap.exception.DuplicateObjectException;
import com.sbaproject.sbamindmap.repository.PackagesRepository;
import com.sbaproject.sbamindmap.repository.TemplateRepository;
import com.sbaproject.sbamindmap.service.PackageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PackageServiceImpl implements PackageService {
    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public Packages getPackageById(long pkgId) {

        return packagesRepository.findById(pkgId).orElseThrow(() -> new DuplicateObjectException("Package not found"));
    }

    @Override
    public Packages setTemplateRelated(long pkgId, long templateId) {
        Packages exitedPackage = packagesRepository.findById(pkgId).orElseThrow(() -> new EntityNotFoundException("Package is not exited"));
        Template relatedTemplate = templateRepository.findById(templateId).orElseThrow(() -> new EntityNotFoundException("Template not found"));
        if (exitedPackage != null && relatedTemplate != null) {
            exitedPackage.getTemplates().add(relatedTemplate);
            return packagesRepository.save(exitedPackage);
        }
        return null;
    }

    @Override
    public List<Packages> getPackages() {
        return packagesRepository.findAll();
    }

    @Override
    @Transactional
    public Packages createPackage(PackageRequest pkg) {
        Packages newPackages = new Packages();
        Packages exitedPackage = packagesRepository.findById(newPackages.getPackageId()).orElse(null);
        if (exitedPackage == null) {
            newPackages.setName(pkg.getName());
            newPackages.setDurationDays(pkg.getDurationDays());
            newPackages.setPrice(pkg.getPrice());
            newPackages.setCreatedAt(LocalDateTime.now());
            newPackages.setUpdatedAt(LocalDateTime.now());
            newPackages.setApiCallLimit(pkg.getApiCallLimit());
            return packagesRepository.save(newPackages);
        } else {
            throw new DuplicateObjectException("Package is exited");
        }
    }

    @Override
    @Transactional
    public Packages updatePackage(PackageRequest pkg , long pkgId) {

        Packages exitedPackages = packagesRepository.findById(pkgId).orElseThrow(() -> new DuplicateObjectException("Package is exited"));
        if (exitedPackages != null) {
            exitedPackages.setName(pkg.getName());
            exitedPackages.setDurationDays(pkg.getDurationDays());
            exitedPackages.setPrice(pkg.getPrice());
            exitedPackages.setCreatedAt(LocalDateTime.now());
            exitedPackages.setUpdatedAt(LocalDateTime.now());
            exitedPackages.setApiCallLimit(pkg.getApiCallLimit());
            return packagesRepository.save(exitedPackages);
        }
        return null;
    }

    @Override
    @Transactional
    public void deletePackage(long pkgId) {
        packagesRepository.deleteById(pkgId);
    }
}
