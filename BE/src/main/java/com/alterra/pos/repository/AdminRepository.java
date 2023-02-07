package com.alterra.pos.repository;

import com.alterra.pos.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query
    List<Admin> findAllByIsValidTrue();

    @Override
    Optional<Admin> findById(Integer id);
}
