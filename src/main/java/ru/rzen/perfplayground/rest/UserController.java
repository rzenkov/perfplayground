package ru.rzen.perfplayground.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rzen.perfplayground.aop.MeasureAndLogSlow;
import ru.rzen.perfplayground.dto.UserDTO;
import ru.rzen.perfplayground.filter.UserFilter;
import ru.rzen.perfplayground.service.UserService;

@Tag(name = "User JPA", description = "Используем JPA")
@RestController
@RequestMapping("/jpa/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MeasureAndLogSlow(limit = 10)
    @GetMapping
    @PageableAsQueryParam
    Page<UserDTO> getUsers(
        @ParameterObject UserFilter filter,
        @Parameter(hidden = true) Pageable pageable
    ) {
        return userService.findAll(filter, pageable)
            .map(UserDTO::of);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("with-work-before")
    @PageableAsQueryParam
    Page<UserDTO> getUsersAndDoWorkBefore(
        @ParameterObject UserFilter filter,
        @Parameter(hidden = true) Pageable pageable
    ) {
        doWork();

        return userService.findAll(filter, pageable)
            .map(UserDTO::of);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("with-work-after")
    @PageableAsQueryParam
    Page<UserDTO> getUsersAndDoWorkAfter(
        @ParameterObject UserFilter filter,
        @Parameter(hidden = true) Pageable pageable
    ) {
        var found = userService.findAll(filter, pageable);

        doWork();

        return found.map(UserDTO::of);
    }

    @SneakyThrows
    private void doWork() {
        Thread.sleep(100);
    }
}
