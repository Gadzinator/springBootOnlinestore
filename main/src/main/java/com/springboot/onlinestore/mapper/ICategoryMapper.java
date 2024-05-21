package com.springboot.onlinestore.mapper;

import com.springboot.onlinestore.domain.dto.CategoryDto;
import com.springboot.onlinestore.domain.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = IProductMapper.class)
public interface ICategoryMapper {

	CategoryDto mapToCategoryDto(Category category);

	Category mapToCategory(CategoryDto categoryDto);
}
