package ru.rzen.perfplayground.filter;

import org.springframework.data.jpa.domain.Specification;
import ru.rzen.perfplayground.domain.User;

public class UserFilter implements Filter<User> {
    @Override
    public Specification<User> toSpecification() {
        return null;
    }
}
