package com.example.multimodal_projcet.repository;

import com.example.multimodal_projcet.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberRepositoryImplTest {

    @Autowired
    EntityManager em;
    MemberRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new MemberRepositoryImpl(em);
    }

    @Test
    @Order(1)
    @Transactional
    @Rollback(false)  // 테스트 간 데이터 유지
    void save() {
        Member member = new Member();
        member.setUsername("aaaaa");
        member.setEmail("aaaaa@gmail.com");
        member.setPassword("aaaaa");
        member.setRole("USER");
        member.setCreatedAt(LocalDateTime.now());
        member.setStatus("ACTIVE");

        repository.save(member);
    }

    @Test
    @Order(2)
    void findByUsername() {
        Optional<Member> foundMember = repository.findByUsername("aaaaa");
        assertThat(foundMember).isPresent();
    }

    @Test
    @Order(3)
    void findAll() {
        List<Member> members = repository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    @Order(4)
    void findByEmail() {
        Optional<Member> foundMember = repository.findByEmail("aaaaa@gmail.com");
        assertThat(foundMember).isPresent();
    }

    @Test
    @Order(5)
    void deleteById() {
        repository.deleteById("aaaaa");
        Optional<Member> foundMember = repository.findByUsername("aaaaa");
        assertThat(foundMember).isNotPresent();
    }
}
