package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.response.GeneratedDataResponse;

import java.util.List;

public interface GeneratedDataService {

    /**
     * Lấy generated data theo ID
     */
    GeneratedDataResponse getGeneratedDataById(Long id);

    /**
     * Lấy tất cả generated data
     */
    List<GeneratedDataResponse> getAllGeneratedData();

    /**
     * Đánh dấu generated data đã được kiểm tra
     */
    GeneratedDataResponse markAsChecked(Long id);

    /**
     * Xóa generated data
     */
    void deleteGeneratedData(Long id);
}
