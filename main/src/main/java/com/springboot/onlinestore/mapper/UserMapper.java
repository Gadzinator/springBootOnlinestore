package com.springboot.onlinestore.mapper;


import com.springboot.onlinestore.domain.dto.UserDto;
import com.springboot.onlinestore.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDto mapToUserDto(User user);

	User mapToUser(UserDto userDto);
}
