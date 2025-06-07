package ru.rzen.perfplayground.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rzen.perfplayground.aop.MeasureAndLogSlow;
import ru.rzen.perfplayground.dto.UserDTO;
import ru.rzen.perfplayground.filter.UserFilter;
import ru.rzen.perfplayground.service.UserService;

@Tag(name = "User JPA", description = "Используем JPA")
@RestController
@RequestMapping("/jpa/users-with-relations")
@RequiredArgsConstructor
public class UserWithRelationsController {
    private final UserService userService;

    @MeasureAndLogSlow(limit = 10)
    @GetMapping
    @PageableAsQueryParam
    Page<UserDTO> getUsersWithRelations(
        @ParameterObject UserFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        return userService.findAllWithRelations(filter, pageable);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("separated")
    @PageableAsQueryParam
    Page<UserDTO> getUsersAndRelations(
        @ParameterObject UserFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        return userService.findAllAndRelations(filter, pageable);
    }

}
