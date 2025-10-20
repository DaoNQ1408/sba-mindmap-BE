package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.PackageRequest;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.entity.Packages;

import java.util.List;

public interface OdersService {
    public Orders createPackage(PackageRequest ord);
    public Orders updatePackage(PackageRequest ord, long orderId);
    public void deletePackage(long orderId);
    public Orders getPackageById(long orderId);
    public List<Orders> getPackages();
}
