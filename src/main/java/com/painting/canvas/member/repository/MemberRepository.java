package com.painting.canvas.member.repository;

import com.painting.canvas.member.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String > {
//    Optional<Member> findAllByGoogleId(Long googleId);
    @Query("{id: '?0'}")
    Optional<Member> findAllByGoogleId(String googleId);

    public long count();

}
