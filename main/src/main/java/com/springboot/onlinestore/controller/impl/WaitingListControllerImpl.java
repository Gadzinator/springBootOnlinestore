package com.springboot.onlinestore.controller.impl;

import com.springboot.onlinestore.controller.WaitingListController;
import com.springboot.onlinestore.domain.dto.WaitingLIstDto;
import com.springboot.onlinestore.service.WaitingListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WaitingListController", description = "Working with waitingLists")
@RequiredArgsConstructor
@RestController
@RequestMapping("/waitingLists")
public class WaitingListControllerImpl implements WaitingListController {

	private final WaitingListService waitingListService;

	@Override
	@PostMapping("/{productId}/{username}")
	public ResponseEntity<?> save(@PathVariable(value = "productId") Long productId, @PathVariable(value = "username") String username) {
		waitingListService.save(productId, username);

		return new ResponseEntity<>("WaitingList save", HttpStatus.CREATED);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable(value = "id") Long id) {
		final WaitingLIstDto waitingLIstDto = waitingListService.findById(id);

		return new ResponseEntity<>(waitingLIstDto, HttpStatus.OK);
	}

	@Override
	@GetMapping()
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
									 @RequestParam(value = "size", defaultValue = "10") int size) {
		final Pageable pageable = PageRequest.of(page, size);
		final Page<WaitingLIstDto> waitingLIstDtoPage = waitingListService.findAll(pageable);

		return new ResponseEntity<>(waitingLIstDtoPage, HttpStatus.OK);
	}

	@Override
	@PutMapping
	public ResponseEntity<?> update(@RequestBody WaitingLIstDto waitingLIstDto) {
		final WaitingLIstDto updateWaitingList = waitingListService.update(waitingLIstDto);

		return new ResponseEntity<>(updateWaitingList, HttpStatus.OK);
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		waitingListService.delete(id);

		return new ResponseEntity<>("WaitingList delete", HttpStatus.ACCEPTED);
	}
}
