package com.springboot.onlinestore.service;

import com.springboot.onlinestore.domain.dto.WaitingLIstDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IWaitingListService {

	void save(long id, String username);

	WaitingLIstDto findById(long id);

	Page<WaitingLIstDto> findAll(Pageable waitingListPageable);

	WaitingLIstDto update(WaitingLIstDto waitingLIstDto);

	void delete(long id);
}
