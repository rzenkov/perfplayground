package ru.rzen.perfplayground.dto;

public record SelectsItem<T>(T key, String label, T value) {

    public SelectsItem(T key, String label) {
        this(key, label, key);
    }

    public static <S> SelectsItem<S> of(S key, String label) {
        return new SelectsItem<>(key, label, key);
    }
}
