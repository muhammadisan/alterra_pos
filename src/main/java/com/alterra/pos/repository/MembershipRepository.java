package com.alterra.pos.repository;

import com.alterra.pos.entity.Category;
import com.alterra.pos.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    @Query
    List<Membership> findAllByIsValidTrue();

    @Override
    Optional<Membership> findById(Integer id);
}
