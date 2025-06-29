package ru.rzen.perfplayground.query;

import static ru.rzen.perflayground.jooq.Tables.POSITION;
import static ru.rzen.perflayground.jooq.Tables.SUBDIVISION;
import static ru.rzen.perflayground.jooq.Tables.USERS;
import static ru.rzen.perflayground.jooq.Tables.USER_TO_POSITION;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.rzen.perfplayground.dto.jooq.UserJooqDTO;
import ru.rzen.perfplayground.filter.JooqFilter;

@Repository
@RequiredArgsConstructor
public class FetchUserPaginatedListQuery implements PaginatedJooqQuery {
    private final DSLContext dsl;

    public Page<UserJooqDTO> handle(JooqFilter filter, Pageable pageable) {
        var condition = filter.toCondition();

        var order = getOrders(pageable.getSort(), Map.of(), USERS.ID.asc());

        var subdivision = DSL.jsonObject(
            DSL.key("key").value(SUBDIVISION.ID),
            DSL.key("label").value(SUBDIVISION.SHORT_NAME)
        );

        var query = dsl.select(
                USERS.ID,
                USERS.LAST_NAME,
                USERS.FIRST_NAME,
                USERS.MIDDLE_NAME,
                subdivision.as("subdivision"),
                DSL.multiset(DSL.select(
                        POSITION.ID,
                        POSITION.NAME
                    )
                    .from(POSITION)
                    .where(POSITION.ID.in(
                        DSL.select(USER_TO_POSITION.POSITION_ID)
                            .from(USER_TO_POSITION)
                            .where(USER_TO_POSITION.USER_ID.eq(USERS.ID))
                    ))).as("positions")
            )
            .from(USERS)
            .leftJoin(SUBDIVISION)
            .on(USERS.SUBDIVISION_ID.eq(SUBDIVISION.ID))
            .where(condition)
            .orderBy(order);

        return getPaginatedResult(
            pageable, query, () -> getCount(condition), UserJooqDTO.class
        );
    }

    private long getCount(Condition condition) {
        var count =  dsl.selectCount()
            .from(USERS)
            .leftJoin(SUBDIVISION)
            .on(USERS.SUBDIVISION_ID.eq(SUBDIVISION.ID))
            .where(condition)
            .fetchOneInto(Long.class);
        return Objects.requireNonNullElse(count, 0L);
    }
}
