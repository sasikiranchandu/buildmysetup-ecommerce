package com.vube.buildMySetup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vube.buildMySetup.model.Cart;
import com.vube.buildMySetup.model.Component;
import com.vube.buildMySetup.model.User;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    void deleteByUser(User user);
    Optional<Cart> findByUserAndComponent(User user, Component component);


}