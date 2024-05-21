package com.springboot.onlinestore.repository;

import com.springboot.onlinestore.domain.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomProductRepositoryImpl implements CustomProductRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Product> findByParams(Map<String, String> params) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Product> query = cb.createQuery(Product.class);

		Root<Product> root = query.from(Product.class);

		List<Predicate> predicates = new ArrayList<>();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			Predicate name = cb.equal(root.get(entry.getKey()), entry.getValue());
			predicates.add(cb.and(name));
		}

		query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
		return entityManager.createQuery(query).getResultList();
	}
}
