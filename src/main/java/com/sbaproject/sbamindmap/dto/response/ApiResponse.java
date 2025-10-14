package com.sbaproject.sbamindmap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean status;
    private T data;
    private String message;


    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }


    public static <T> ApiResponse<T> error(T data, String message) {
        return new ApiResponse<>(false, data, message);
    }
}