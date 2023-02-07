package com.alterra.pos.controller;

import com.alterra.pos.entity.Membership;
import com.alterra.pos.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("membership")
public class MembershipController {
    @Autowired
    private MembershipRepository membershipRepository;

    @GetMapping
    public List<Membership> getMembership() {
        return membershipRepository.findAllByIsValidTrue();
    }

    @GetMapping("/{id}")
    public Optional<Membership> getMembershipById(@PathVariable Integer id) { return membershipRepository.findById(id); }

    @PostMapping
    public Membership addMembership(@Validated @RequestBody Membership membership) {
        return membershipRepository.save(membership);
    }
}
