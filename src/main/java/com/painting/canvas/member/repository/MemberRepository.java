package com.painting.canvas.member.repository;

import com.painting.canvas.member.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String > {
//    Optional<Member> findAllByGoogleId(Long googleId);

    public Member findAllByGoogleId(String googleId);


}
