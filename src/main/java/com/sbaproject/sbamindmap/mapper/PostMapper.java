package com.sbaproject.sbamindmap.mapper;

import com.sbaproject.sbamindmap.dto.request.PostRequest;
import com.sbaproject.sbamindmap.dto.response.PostResponse;
import com.sbaproject.sbamindmap.entity.Post;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostRequest request);
    PostResponse toResponse(Post entity);
    List<PostResponse> toResponseList(List<Post> entities);
}
