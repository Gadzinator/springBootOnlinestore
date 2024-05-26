package com.springboot.onlinestore.mapper;

import com.springboot.onlinestore.domain.dto.WaitingLIstDto;
import com.springboot.onlinestore.domain.entity.User;
import com.springboot.onlinestore.domain.entity.WaitingList;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface WaitingListMapper {

	@Mapping(source = "user", target = "username", qualifiedByName = "mapUserToUsername")
	@Mapping(source = "product", target = "productDto")
	WaitingLIstDto mapToWaitingListDto(WaitingList waitingList);

	@InheritInverseConfiguration
	@Mapping(source = "username", target = "user", qualifiedByName = "mapUsernameToUser")
	@Mapping(source = "productDto", target = "product")
	WaitingList mapToWaitingList(WaitingLIstDto waitingLIstDto);

	@Named("mapUserToUsername")
	default String mapUserToUsername(User user) {
		return user.getName();
	}

	@Named("mapUsernameToUser")
	default User mapUsernameToUser(String username) {
		User user = new User();
		user.setName(username);
		return user;
	}
}
