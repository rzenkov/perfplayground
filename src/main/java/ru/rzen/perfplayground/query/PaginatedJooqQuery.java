package ru.rzen.perfplayground.query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongSupplier;
import org.jooq.Field;
import org.jooq.SelectLimitStep;
import org.jooq.SortField;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

public interface PaginatedJooqQuery {
    /**
     * Получить `paginated` результат по Jooq запросу.
     *
     * @param <T> тип строки результата
     * @param pageable  параметр пагинации
     * @param query запрос Jooq DSL
     * @param countSupplier функция возвращающая общее количество элементов
     * @param clazz Класс в который будут смаплены строки результата запроса
     * @return пагинированный результат
     */
    default <T> PageImpl<T> getPaginatedResult(
        Pageable pageable,
        SelectLimitStep<?> query,
        LongSupplier countSupplier,
        Class<T> clazz
    ) {
        if (pageable.isUnpaged()) {
            List<T> result = query
                .fetchInto(clazz);

            return new PageImpl<>(result);
        }

        long total = countSupplier.getAsLong();
        List<T> result = query
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetchInto(clazz);

        return new PageImpl<>(result, pageable, total);
    }

    /**
     * Сгенерировать сортировку для Jooq Запроса по данным Spring Sort и сопоставлению.
     *
     * @param sort Spring Sort
     * @param sortFields сопоставление полей Sort к полям JOOQ
     * @param defaultOrder сортировка по умолчанию
     * @return сортировка для запроса Jooq
     */
    @SuppressWarnings("java:S1452")
    default List<SortField<?>> getOrders(
        Sort sort, Map<String, Field<?>> sortFields, SortField<?>... defaultOrder
    ) {
        if (sort.isUnsorted()) {
            return Arrays.asList(defaultOrder);
        }

        List<SortField<?>> orders = sort.stream()
            .map(s -> {
                if (sortFields.containsKey(s.getProperty().toUpperCase())) {
                    var field = sortFields.get(s.getProperty().toUpperCase());
                    return s.isAscending() ? field.asc() : field.desc();
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();

        return ObjectUtils.isEmpty(orders)
            ? Arrays.asList(defaultOrder)
            : orders;
    }
}
