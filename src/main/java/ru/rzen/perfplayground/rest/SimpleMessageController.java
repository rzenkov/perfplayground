package ru.rzen.perfplayground.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rzen.perfplayground.aop.MeasureAndLogSlow;

@Tag(name = "Simple", description = "Без использования базы данных")
@RestController
@RequestMapping("/simple/messages")
@RequiredArgsConstructor
public class SimpleMessageController {

    @SneakyThrows
    @MeasureAndLogSlow(limit = 10)
    @GetMapping
    @PageableAsQueryParam
    Page<SimpleMessage> getMessages(
        @Parameter(hidden = true) Pageable pageable
    ) {
        var messages = IntStream.range(0, pageable.getPageSize())
            .mapToObj(i -> new SimpleMessage(
                UUID.randomUUID(),
                "Message %d".formatted((i + 1) * pageable.getPageSize())))
            .toList();
        return new PageImpl<>(messages, pageable, 153);
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("with-work")
    @PageableAsQueryParam
    Page<SimpleMessage> getMessagesAndDoWork(
        @Parameter(hidden = true) Pageable pageable
    ) {
        var messages = IntStream.range(0, pageable.getPageSize())
            .mapToObj(i -> new SimpleMessage(
                UUID.randomUUID(),
                "Message %d".formatted((i + 1) * pageable.getPageSize())))
            .toList();
        doWork();

        return new PageImpl<>(messages, pageable, 153);
    }

    @SneakyThrows
    private void doWork() {
        Thread.sleep(100);
    }

    record SimpleMessage(UUID id, String message) {}
}
