package ru.rzen.perfplayground.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.rzen.perfplayground.domain.User;

public interface UserWithRelationsRepository extends
    JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    @Override
    @EntityGraph(attributePaths = {"subdivision", "positions"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"subdivision", "positions"})
    List<User> findUserByIdIn(List<UUID> ids, Sort sort);
}
