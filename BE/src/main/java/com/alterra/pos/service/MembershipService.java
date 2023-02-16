package com.alterra.pos.service;

import com.alterra.pos.entity.Membership;
import com.alterra.pos.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {
    @Autowired
    private MembershipRepository membershipRepository;

    public List<Membership> getMembership() {
        return membershipRepository.findAllByIsValidTrue();
    }

    public Optional<Membership> getMembershipById(Integer id) {
        return membershipRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Membership addMembership(Membership membership) {
        return membershipRepository.save(membership);
    }
}
