package ru.rzen.perfplayground.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ru.rzen.perfplayground.domain.User;

/**
 * Представление сущности {@link User}
 *
 * @param id идентификатор
 * @param lastName фамилия
 * @param firstName имя
 * @param middleName отчество
 * @param subdivision подразделение
 * @param positions должности
 * @param isManager является ли менеджером
 */
@Schema(description = "Представление сущности User")
public record UserDTO(
    @Schema(description = "Идентификатор")
    UUID id,
    @Schema(description = "Фамилия")
    String lastName,
    @Schema(description = "Имя")
    String firstName,
    @Schema(description = "Отчество")
    String middleName,
    @Schema(description = "Подразделение")
    SelectsItem<UUID> subdivision,
    @Schema(description = "Должности")
    List<SelectsItem<UUID>> positions,
    @Schema(description = "Является ли менеджером")
    boolean isManager
) {
    public static UserDTO of(User user) {
        var subdivision = Optional.ofNullable(user.getSubdivision())
            .map(el -> SelectsItem.of(el.getId(), el.getShortName()))
            .orElse(null);
        var positions = user.getPositions().stream()
            .map(el -> SelectsItem.of(el.getId(), el.getName()))
            .sorted(Comparator.comparing(SelectsItem::label))
            .toList();

        return new UserDTO(
            user.getId(),
            user.getLastName(),
            user.getFirstName(),
            user.getMiddleName(),
            subdivision,
            positions,
            user.isManager()
        );
    }


}
