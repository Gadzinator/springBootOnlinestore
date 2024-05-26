package com.springboot.onlinestore.mapper;

import com.springboot.onlinestore.domain.dto.ProductDto;
import com.springboot.onlinestore.domain.entity.Product;
import com.springboot.onlinestore.utils.DateConstant;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

	@Mapping(source = "created", target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "received", target = "received", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "category.name", target = "category")
	ProductDto mapToProductDto(Product product);

	@InheritInverseConfiguration
	@Mapping(source = "created", target = "created", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "received", target = "received", dateFormat = DateConstant.DEFAULT_DATE_PATTERN)
	@Mapping(source = "category", target = "category.name")
	Product mapToProduct(ProductDto productDto);
}
