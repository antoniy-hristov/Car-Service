package com.carservice.services;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class BaseService {

    @Autowired
    protected ModelMapper modelMapper;

    protected <D, T> D map(T source, Class<D> destinationClass) {
        if (ObjectUtils.anyNull(source, destinationClass)) {
            return null;
        }
        return modelMapper.map(source, destinationClass);
    }

    protected <D, T> List<D> mapToList(Collection<T> sourceList, Class<D> destinationClass) {
        if (CollectionUtils.isEmpty(sourceList) || ObjectUtils.anyNull(destinationClass)) {
            return Collections.emptyList();
        }
        return sourceList.stream()
                .map(source -> map(source, destinationClass))
                .collect(Collectors.toList());
    }
}
