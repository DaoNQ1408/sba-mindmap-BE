package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.CollectionRequest;
import com.sbaproject.sbamindmap.dto.response.CollectionResponse;
import com.sbaproject.sbamindmap.entity.Collection;

import java.util.List;

public interface CollectionService {
    Collection findById(long collectionId);
    CollectionResponse findResponseById(long collectionId);
    List<CollectionResponse> findAllCollections();
    CollectionResponse savedCollection(CollectionRequest collectionRequest);
    CollectionResponse updateCollection(long collectionId, CollectionRequest collectionRequest);
    CollectionResponse deleteCollection(long collectionId);
}
