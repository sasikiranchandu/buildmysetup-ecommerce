package com.vube.buildMySetup.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vube.buildMySetup.model.Component;

public interface ComponentRepo  extends JpaRepository<Component, Long>{
	

	List<Component> findByNameContainingIgnoreCase(String keyword, Sort sort);}
