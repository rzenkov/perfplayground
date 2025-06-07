package ru.rzen.perfplayground.dto.jooq;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserJooqDTO {
    UUID id;
    String lastName;
    String firstName;
    String middleName;
    CatalogItemDTO subdivision;
    List<CatalogItemDTO> positions;
    boolean manager;
}
