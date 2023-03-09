package com.painting.canvas.config.jwt.refresh;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, Long> {
    void deleteByMember_Id(Long memberId);

    @EntityGraph(attributePaths = {"member"})
    Optional<RefreshToken> findByTokenValue(String tokenValue);
}
