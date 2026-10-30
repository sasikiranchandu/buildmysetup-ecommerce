package com.vube.buildMySetup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vube.buildMySetup.model.Component;
import com.vube.buildMySetup.repository.ComponentRepo;

@Service
public class Componentservice {

	@Autowired
	private ComponentRepo componentRepo; // ✅ naming fixed

	// 👉 1. Get All Components
	public List<Component> getAllComponents() {
		return componentRepo.findAll(Sort.by(Sort.Direction.ASC, "price"));
	}

	// 👉 2. Add Component
	public Component addComponent(Component component) {

		validateComponent(component);

		return componentRepo.save(component);
	}

	// 👉 3. Get Component By ID
	public Component getComponentById(Long id) {
		return componentRepo.findById(id).orElseThrow(() -> new RuntimeException("Component not found with id: " + id));
	}

	// 👉 4. Delete Component
	public void deleteComponent(Long id) {
		if (!componentRepo.existsById(id)) {
			throw new RuntimeException("Component not found with id: " + id);
		}
		componentRepo.deleteById(id);
	}

	// 👉 5. Update Component
	public Component updateComponent(Component component) {

		if (component.getId() == null) {
			throw new RuntimeException("Component ID is required for update");
		}

		// check if exists
		Component existing = getComponentById(component.getId());

		validateComponent(component);

		// 🔁 Update fields
		existing.setName(component.getName());
		existing.setCategory(component.getCategory());
		existing.setPrice(component.getPrice());
		existing.setType(component.getType());
		existing.setStock(component.getStock());
		existing.setImage(component.getImage()); // for BLOB

		return componentRepo.save(existing);
	}

	// 👉 Common Validation Method
	private void validateComponent(Component component) {

		if (component.getName() == null || component.getName().isBlank()) {
			throw new RuntimeException("Component name cannot be empty");
		}

		if (component.getPrice() == null || component.getPrice() <= 0) {
			throw new RuntimeException("Price must be greater than 0");
		}

		if (component.getStock() == null || component.getStock() < 0) {
			throw new RuntimeException("Stock cannot be negative");
		}
	}

	public Component getComponentOrNull(Long id) {
		return componentRepo.findById(id).orElse(null);
	}

	public List<Component> searchComponent(String keyword) {
		return componentRepo.findByNameContainingIgnoreCase(keyword, Sort.by(Sort.Direction.ASC, "price") // 🔥 lowest
																											// price
																											// first
		);
	}
}