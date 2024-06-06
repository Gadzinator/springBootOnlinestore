package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Product;
import com.springboot.onlinestore.domain.entity.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {

	Optional<WaitingList> findByProduct(Product product);
}
