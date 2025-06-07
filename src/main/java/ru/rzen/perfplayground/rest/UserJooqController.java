package ru.rzen.perfplayground.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rzen.perfplayground.aop.MeasureAndLogSlow;
import ru.rzen.perfplayground.dto.jooq.UserJooqDTO;
import ru.rzen.perfplayground.filter.UserJooqFilter;
import ru.rzen.perfplayground.query.FetchUserPaginatedListMinJsonQuery;
import ru.rzen.perfplayground.query.FetchUserPaginatedListQuery;
import ru.rzen.perfplayground.query.FetchUserPaginatedSliceMinJsonQuery;
import ru.rzen.perfplayground.query.FetchUserPaginatedSliceQuery;

@Tag(name = "User JOOQ", description = "Используем JOOQ")
@RestController
@RequestMapping("/jooq/users")
@RequiredArgsConstructor
public class UserJooqController {
    private final FetchUserPaginatedListQuery fetchUserPaginatedListQuery;
    private final FetchUserPaginatedListMinJsonQuery fetchUserPaginatedListMinJsonQuery;
    private final FetchUserPaginatedSliceQuery fetchUserPaginatedSliceQuery;
    private final FetchUserPaginatedSliceMinJsonQuery fetchUserPaginatedSliceMinJsonQuery;

    @MeasureAndLogSlow(limit = 10)
    @GetMapping
    @PageableAsQueryParam
    Page<UserJooqDTO> getUsers(
        @ParameterObject UserJooqFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        return fetchUserPaginatedListMinJsonQuery.handle(filter, pageable);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("min-json")
    @PageableAsQueryParam
    Page<UserJooqDTO> getUsersMinJson(
        @ParameterObject UserJooqFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        return fetchUserPaginatedListMinJsonQuery.handle(filter, pageable);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("slice")
    @PageableAsQueryParam
    Slice<UserJooqDTO> getUsersSlice(
        @ParameterObject UserJooqFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        return fetchUserPaginatedSliceQuery.handle(filter, pageable);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("slice-min-json")
    @PageableAsQueryParam
    Slice<UserJooqDTO> getUsersSliceMinJson(
        @ParameterObject UserJooqFilter filter,
        @PageableDefault(sort = "id", size = 20)
        @Parameter(hidden = true) Pageable pageable
    ) {
        return fetchUserPaginatedSliceMinJsonQuery.handle(filter, pageable);
    }
}
