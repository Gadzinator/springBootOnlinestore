package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Order> findOrdersByProductId(long productId) {
		return entityManager.createQuery(
						"SELECT o FROM Order o JOIN o.products p WHERE p.id = :productId",
						Order.class)
				.setParameter("productId", productId)
				.getResultList();
	}
}
