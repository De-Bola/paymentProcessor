package com.luminor.paymentApp.service.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.NullValueMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MapperInterface<MDL, DTO>{
    DTO toDto(MDL model);
    MDL toModel(DTO dto);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<MDL> toModels(List<DTO> dtos);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<DTO> toDtos(List<MDL> models);

/*    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    Page<MDL> toModels(Page<DTO> dtos);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    Page<DTO> toDtos(Page<MDL> models);*/
}
