package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.PackageRequest;
import com.sbaproject.sbamindmap.entity.Packages;

import java.util.List;

public interface PackageService {
    public Packages createPackage(PackageRequest pkg);
    public Packages updatePackage(PackageRequest pkg, long pkgId);
    public void deletePackage(long pkgId);
    public Packages getPackageById(long pkgId);
    public List<Packages> getPackages();
}
