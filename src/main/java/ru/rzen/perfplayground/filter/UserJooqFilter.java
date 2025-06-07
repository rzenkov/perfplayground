package ru.rzen.perfplayground.filter;

import org.jooq.Condition;
import org.jooq.impl.DSL;

public class UserJooqFilter implements JooqFilter {

    @Override
    public Condition toCondition() {
        return DSL.noCondition();
    }
}
