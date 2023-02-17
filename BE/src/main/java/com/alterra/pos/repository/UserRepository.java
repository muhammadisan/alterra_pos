package com.alterra.pos.repository;

import com.alterra.pos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query
    List<User> findAllByIsValidTrue();

    @Override
    Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);
}
