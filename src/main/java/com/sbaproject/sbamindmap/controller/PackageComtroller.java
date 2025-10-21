package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.PackageRequest;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.entity.Packages;
import com.sbaproject.sbamindmap.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
public class PackageComtroller {

    @Autowired
    private PackageService packageService;

    @PostMapping("/")
    public ResponseEntity<ResponseBase> createPackage(@RequestBody PackageRequest pkg) {
        Packages packages = packageService.createPackage(pkg);
        if (packages != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Package created successfully", packages));
        } else {
            return ResponseEntity.status(500).body(new ResponseBase(409, "Failed to create package", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBase> updatePackage(@PathVariable long id, @RequestBody PackageRequest pkg) {
        Packages updatedPackage = packageService.updatePackage(pkg, id);
        if (updatedPackage != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Package updated successfully", updatedPackage));
        } else {
            return ResponseEntity.status(500).body(new ResponseBase(409, "Failed to update package", null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseBase> patchPackage(@PathVariable long id) {
        try {
            packageService.deletePackage(id);
            return ResponseEntity.ok(new ResponseBase(200, "Delete Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to delete", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase> getPackageById(@PathVariable long id) {
        Packages pkg = packageService.getPackageById(id);
        if (pkg != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Package retrieved successfully", pkg));
        } else {
            return ResponseEntity.status(404).body(new ResponseBase(404, "Package not found", null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseBase> getAllPackages() {
        return ResponseEntity.ok(new ResponseBase(200, "Packages retrieved successfully", packageService.getPackages()));
    }

    @PostMapping("/{pkgId}/template/{templateId}")
    public ResponseEntity<ResponseBase> setTemplateToPackage(@PathVariable long pkgId, @PathVariable long templateId) {
        Packages updatedPackage = packageService.setTemplateRelated(pkgId, templateId);
        if (updatedPackage != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Template set to package successfully", updatedPackage));
        } else {
            return ResponseEntity.status(500).body(new ResponseBase(409, "Failed to set template to package", null));
        }
    }
}
