package ru.rzen.perfplayground.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class SimpleMessage extends Identifiable {
    @Column(name = "body")
    private String body;
}
