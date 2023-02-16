package com.alterra.pos.controller;

import com.alterra.pos.entity.Membership;
import com.alterra.pos.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("membership")
public class MembershipController {
    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public List<Membership> getMembership() {
        return membershipService.getMembership();
    }

    @GetMapping("/{id}")
    public Optional<Membership> getMembershipById(@PathVariable Integer id) {
        return membershipService.getMembershipById(id);
    }

    @PostMapping
    public Membership addMembership(@Validated @RequestBody Membership membership) {
        return membershipService.addMembership(membership);
    }
}
