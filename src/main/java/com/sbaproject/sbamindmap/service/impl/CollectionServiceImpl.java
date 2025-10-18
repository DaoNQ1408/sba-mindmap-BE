package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.CollectionRequest;
import com.sbaproject.sbamindmap.dto.response.CollectionResponse;
import com.sbaproject.sbamindmap.entity.Collection;
import com.sbaproject.sbamindmap.exception.DuplicateObjectException;
import com.sbaproject.sbamindmap.mapper.CollectionMapper;
import com.sbaproject.sbamindmap.repository.CollectionRepository;
import com.sbaproject.sbamindmap.service.CollectionService;
import com.sbaproject.sbamindmap.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;
    private final UserService userService;

    @Override
    public Collection findById(long collectionId) {
        return collectionRepository.findById(collectionId).orElseThrow(() ->
                new EntityNotFoundException(
                        "Collection not found with id: " +
                                collectionId)
        );
    }

    @Override
    public CollectionResponse findResponseById(long collectionId) {

        Collection collection = findById(collectionId);

        return collectionMapper.toResponse(collection);
    }

    @Override
    public List<CollectionResponse> findAllCollections() {
        return collectionRepository.findAll().stream()
                .map(collectionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CollectionResponse savedCollection(CollectionRequest collectionRequest) {

        validateCollectionNameUniqueness(collectionRequest.getName());

        Collection collection = collectionMapper.toEntity(collectionRequest);

        Collection savedCollection = collectionRepository.save(collection);

        return collectionMapper.toResponse(savedCollection);
    }

    @Override
    @Transactional
    public CollectionResponse updateCollection(long collectionId,
                                               CollectionRequest collectionRequest) {

        Collection collection = findById(collectionId);

        collectionMapper.updateEntityFromRequest(collection, collectionRequest);

        Collection updatedCollection = collectionRepository.save(collection);

        return collectionMapper.toResponse(updatedCollection);
    }

    @Override
    @Transactional
    public CollectionResponse deleteCollection(long collectionId) {

        Collection collection = findById(collectionId);

        collectionRepository.delete(collection);

        return collectionMapper.toResponse(collection);
    }

    public void validateCollectionNameUniqueness(String collectionName) {
        collectionRepository.findByName(collectionName)
                .ifPresent(collection -> {
                    throw new DuplicateObjectException(
                            "Collection " +
                                    collectionName +
                                    " already exists");
                });
    }
}
