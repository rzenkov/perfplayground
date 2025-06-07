package ru.rzen.perfplayground.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rzen.perfplayground.domain.Identifiable;
import ru.rzen.perfplayground.dto.UserDTO;
import ru.rzen.perfplayground.filter.UserFilter;
import ru.rzen.perfplayground.repository.UserRepository;
import ru.rzen.perfplayground.repository.UserWithRelationsRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserWithRelationsRepository userWithRelationsRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(UserFilter filter, Pageable pageable) {
        return userRepository.findAll(filter.toSpecification(), pageable)
            .map(UserDTO::of);
    }

    /**
     * EntityGraph на findAll
     *
     * @param filter фильтр
     * @param pageable параметры пагинации
     * @return пользователи
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllWithRelations(UserFilter filter, Pageable pageable) {
        return userWithRelationsRepository.findAll(filter.toSpecification(), pageable)
            .map(UserDTO::of);
    }

    /**
     * EntityGraph на findUserByIdIn
     *
     * @param filter фильтр
     * @param pageable параметры пагинации
     * @return пользователи
     */
    public Page<UserDTO> findAllAndRelations(UserFilter filter, Pageable pageable) {
        var found = userRepository.findAll(filter.toSpecification(), pageable);

        var ids = found.map(Identifiable::getId).getContent();
        var withRelations = userWithRelationsRepository.findUserByIdIn(ids, pageable.getSort())
            .stream().map(UserDTO::of).toList();

        return new PageImpl<>(withRelations, pageable, found.getTotalElements());
    }

}
