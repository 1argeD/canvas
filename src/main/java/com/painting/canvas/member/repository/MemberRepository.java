package com.painting.canvas.member.repository;

import com.painting.canvas.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findAllByGoogleId(Long googleId);
}
