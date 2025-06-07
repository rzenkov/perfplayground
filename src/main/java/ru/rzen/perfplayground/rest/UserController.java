package ru.rzen.perfplayground.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.task.VirtualThreadTaskExecutor;
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
@RequestMapping("/jpa/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final VirtualThreadTaskExecutor virtualThreadTaskExecutor;

    @MeasureAndLogSlow(limit = 10)
    @GetMapping
    @PageableAsQueryParam
    Page<UserDTO> getUsers(
        @ParameterObject UserFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        return userService.findAll(filter, pageable);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("with-work-before")
    @PageableAsQueryParam
    Page<UserDTO> getUsersAndDoWorkBefore(
        @ParameterObject UserFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        doWork();

        return userService.findAll(filter, pageable);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("with-work-after")
    @PageableAsQueryParam
    Page<UserDTO> getUsersAndDoWorkAfter(
        @ParameterObject UserFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        var found = userService.findAll(filter, pageable);

        doWork();

        return found;
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("with-work-parallel")
    @PageableAsQueryParam
    CompletableFuture<Page<UserDTO>> getUsersAndDoWorkParallel(
        @ParameterObject UserFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        var usersFeature = CompletableFuture
            .supplyAsync(() -> userService.findAll(filter, pageable));

        var workFeature = CompletableFuture
            .supplyAsync(() -> {
                doWork();
                return "YES";
            }, virtualThreadTaskExecutor);

        var waitingForWorkDone = CompletableFuture.allOf(
            usersFeature, workFeature
        );

        return waitingForWorkDone.thenApply(blank ->
                Pair.of(usersFeature.join(), workFeature.join()))
            .thenApply(pair -> {
                if (pair.getRight().equals("YES")) {
                    return pair.getLeft();
                }
                return Page.empty();
            });
    }

    @SneakyThrows
    private void doWork() {
        Thread.sleep(100);
    }
}
