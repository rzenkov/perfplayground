package ru.rzen.perfplayground.filter;


import org.jooq.Condition;

public interface JooqFilter {

    Condition toCondition();
}
