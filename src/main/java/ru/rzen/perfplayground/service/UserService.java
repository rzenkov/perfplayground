package ru.rzen.perfplayground.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.rzen.perfplayground.domain.User;
import ru.rzen.perfplayground.filter.UserFilter;
import ru.rzen.perfplayground.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<User> findAll(UserFilter filter, Pageable pageable) {
        return userRepository.findAll(filter.toSpecification(), pageable);
    }
}
