create table if not exists messages (
    id uuid primary key,
    body varchar(255)
);

insert into messages(id, body)
select gen_random_uuid(), 'Message with any text and number # ' || TO_CHAR(gs, 'fm00000') from generate_series(1, 10000) gs
where (select count(*) from messages) < 1000