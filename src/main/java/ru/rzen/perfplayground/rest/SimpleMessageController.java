package ru.rzen.perfplayground.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rzen.perfplayground.aop.MeasureAndLogSlow;
import ru.rzen.perfplayground.repository.SimpleMessageRepository;

@Tag(name = "Simple", description = "Без использования базы данных")
@RestController
@RequestMapping("/simple/messages")
@RequiredArgsConstructor
public class SimpleMessageController {
    private final SimpleMessageRepository messageRepository;
    private final SimpleMessageRepository simpleMessageRepository;

    @SneakyThrows
    @MeasureAndLogSlow(limit = 10)
    @GetMapping
    @PageableAsQueryParam
    Page<SimpleMessage> getMessages(
        @Parameter(hidden = true) Pageable pageable
    ) {
        return simpleMessageRepository.findAll(pageable)
            .map(el -> new SimpleMessage(el.getId(), el.getBody()));
    }

    @MeasureAndLogSlow(limit = 10)
    @GetMapping("with-work")
    @PageableAsQueryParam
    Page<SimpleMessage> getMessagesAndDoWork(
        @Parameter(hidden = true) Pageable pageable
    ) {
        doWork();

        return simpleMessageRepository.findAll(pageable)
            .map(el -> new SimpleMessage(el.getId(), el.getBody()));
    }

    @SneakyThrows
    private void doWork() {
        Thread.sleep(100);
    }

    record SimpleMessage(UUID id, String message) {}
}
