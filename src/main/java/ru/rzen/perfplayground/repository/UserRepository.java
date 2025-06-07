package ru.rzen.perfplayground.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.rzen.perfplayground.domain.User;

public interface UserRepository extends
    JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
}
