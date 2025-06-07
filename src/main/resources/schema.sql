create table if not exists messages
(
    id   uuid primary key,
    body varchar(255)
);

insert into messages(id, body)
select gen_random_uuid(), 'Message with any text and number # ' || TO_CHAR(gs, 'fm00000')
from generate_series(1, 10000) gs
where (select count(*) from messages) < 1000;


create table if not exists subdivision
(
    id         uuid primary key,
    short_name varchar(255),
    path       varchar(10000),
    parent_id  uuid
);

create table if not exists users
(
    id             uuid primary key,
    subdivision_id uuid,
    first_name     varchar(255),
    last_name      varchar(255),
    middle_name    varchar(255)
);

create table if not exists position
(
    id   uuid primary key,
    name varchar(255),
    type varchar(25)
);

create table if not exists user_to_position
(
    user_id     uuid,
    position_id uuid
);

DO '
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = lower(''FK_USERTOPOSITION_ON_USERS''))
    THEN
        alter table user_to_position
            ADD CONSTRAINT FK_USERTOPOSITION_ON_USERS
                FOREIGN KEY (user_id) REFERENCES users (id);
    END IF;
END ';

DO '
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = lower(''FK_USERTOPOSITION_ON_POSITION''))
    THEN
        alter table user_to_position
            ADD CONSTRAINT FK_USERTOPOSITION_ON_POSITION
                FOREIGN KEY (position_id) REFERENCES position (id);
    END IF;
END ';

DO '
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = lower(''FK_USERS_ON_SUBDIVISION''))
    THEN
        alter table users
            ADD CONSTRAINT FK_USERS_ON_SUBDIVISION
                FOREIGN KEY (subdivision_id) REFERENCES subdivision (id);
    END IF;
END ';


insert into position(id, name, type)
select gen_random_uuid(),
       array_to_string(
               array [(array ['Главный', null])[i % 2], (array ['Бухгалтер', 'Дворник', 'Завхоз', 'Слесарь', 'Программист', 'Администратор', 'Тестировщик', 'Менеджер по продажам', 'Менеджер по закупкам', 'Начальник траспортного цеха'])[i % 10 + 1]],
               ' '),
       case when random() > 0.3 then 'EMPLOYEE' else 'MANAGER' end
from generate_series(0, 30) i
WHERE (SELECT count(*) FROM position) < 10;

insert into subdivision(id, short_name, path, parent_id)
select z.id,
       z.short_name,
       array_to_string(array [(select s2.path from subdivision s2 where s2.id = z.parent[1]), z.id::varchar], ' '),
       z.parent[1]
from (select (select array [id, gen_random_uuid()] as rnd from subdivision order by random() limit 1)                                                                                                                                                                                             parent,
             gen_random_uuid()                                                                                                                                                                                                                                                                 as id,
             coalesce(
                     (array ['ООО "Василёк"', 'ООО "Кофейня"', 'ООО "Вторчермет"', 'ООО "Альдебаран"', 'ООО "СтройВсем"', 'ООО "Рога и Копыта"', 'ООО "Сидоров и Ко"', 'ООО "Эквилибрист"', 'ООО "Мастер"', 'ООО "Субмарина"', 'ООО "Здоровье"', 'ООО "Анжелика"', null, null, null, null, null, null, null, null, null, null, null, null])[(floor(random() * 20) + 1)],
                     array_to_string(
                             array [(array ['Мясокомбинат', 'Молокозавод', 'Швейная фабрика', 'Завод металлических конструкций', 'Кабельный завод', 'Судоремонтный завод', 'Производственной объединение', 'Завод электромеханический', 'Станкостроительный завод', 'Производство пластиковых изделий', 'Консервный завод', 'Птицеферма'])[(floor(random() * 12) + 1)],
                                 array_to_string(
                                         array [(array ['№ 1', '№ 2', '№ 3', '№ 4', '№ 5', '№ 6', '№ 7', '№ 8', '№ 9', '№ 10', '№ 11', '№ 12', null, null, null, null, null, null])[(floor(random() * 18) + 1)],
                                             (array ['имени Микояна', 'имени Ленина', 'имени Кирова', 'имени Свердлова', 'Московский', 'Санкт-Петербургский', 'Солекамский', 'имени 29 февраля', 'имени Пушкина', 'на Зеленой', 'на Красной', 'на Синей', null, null, null, null, null, null])[(floor(random() * 18) + 1)]],
                                         ' ')], ' '))
                 || ', ' ||
             (array ['Бухгалтерия', 'Транспортный отдел', 'Логистический отдел', 'Отдел продаж', 'Отдел закупок', 'Отдел проектирования', 'Сборочный цех', 'Производственный цех', 'Отдел кадров', 'Отдел маркетинга', 'Юридический отдел', 'Отдел эксплуатации'])[(floor(random() * 12) + 1)] as short_name
      from generate_series(1, 10)) z
WHERE (SELECT count(*) FROM subdivision) < 10;


insert into subdivision(id, short_name, path, parent_id)
select f.id,
       f.short_name,
       array_to_string(array [(select s2.path from subdivision s2 where s2.id = f.parent), f.id::varchar], ' '),
       f.parent
from (select distinct on (z.id) p.id as parent, z.id, z.short_name
      from (select s.id from subdivision s order by random() limit 100) p
               left join
           (select gen_random_uuid()                                                                                                                                                                                                                                                                 as id,
                   coalesce(
                           (array ['ООО "Василёк"', 'ООО "Кофейня"', 'ООО "Вторчермет"', 'ООО "Альдебаран"', 'ООО "СтройВсем"', 'ООО "Рога и Копыта"', 'ООО "Сидоров и Ко"', 'ООО "Эквилибрист"', 'ООО "Мастер"', 'ООО "Субмарина"', 'ООО "Здоровье"', 'ООО "Анжелика"', null, null, null, null, null, null, null, null, null, null, null, null])[(floor(random() * 20) + 1)],
                           array_to_string(
                                   array [(array ['Мясокомбинат', 'Молокозавод', 'Швейная фабрика', 'Завод металлических конструкций', 'Кабельный завод', 'Судоремонтный завод', 'Производственной объединение', 'Завод электромеханический', 'Станкостроительный завод', 'Производство пластиковых изделий', 'Консервный завод', 'Птицеферма'])[(floor(random() * 12) + 1)],
                                       array_to_string(
                                               array [(array ['№ 1', '№ 2', '№ 3', '№ 4', '№ 5', '№ 6', '№ 7', '№ 8', '№ 9', '№ 10', '№ 11', '№ 12', null, null, null, null, null, null])[(floor(random() * 18) + 1)],
                                                   (array ['имени Микояна', 'имени Ленина', 'имени Кирова', 'имени Свердлова', 'Московский', 'Санкт-Петербургский', 'Солекамский', 'имени 29 февраля', 'имени Пушкина', 'на Зеленой', 'на Красной', 'на Синей', null, null, null, null, null, null])[(floor(random() * 18) + 1)]],
                                               ' ')], ' '))
                       || ', ' ||
                   (array ['Бухгалтерия', 'Транспортный отдел', 'Логистический отдел', 'Отдел продаж', 'Отдел закупок', 'Отдел проектирования', 'Сборочный цех', 'Производственный цех', 'Отдел кадров', 'Отдел маркетинга', 'Юридический отдел', 'Отдел эксплуатации'])[(floor(random() * 12) + 1)] as short_name
            from generate_series(1, (select count(*) from subdivision))) z on true
      order by z.id, z.short_name) f
WHERE (SELECT count(*) FROM subdivision) < 300;

insert into subdivision(id, short_name, path, parent_id)
select f.id,
       f.short_name,
       array_to_string(array [(select s2.path from subdivision s2 where s2.id = f.parent), f.id::varchar], ' '),
       f.parent
from (select distinct on (z.id) p.id as parent, z.id, z.short_name
      from (select s.id from subdivision s order by random() limit 100) p
               left join
           (select gen_random_uuid()                                                                                                                                                                                                                                                                 as id,
                   coalesce(
                           (array ['ООО "Василёк"', 'ООО "Кофейня"', 'ООО "Вторчермет"', 'ООО "Альдебаран"', 'ООО "СтройВсем"', 'ООО "Рога и Копыта"', 'ООО "Сидоров и Ко"', 'ООО "Эквилибрист"', 'ООО "Мастер"', 'ООО "Субмарина"', 'ООО "Здоровье"', 'ООО "Анжелика"', null, null, null, null, null, null, null, null, null, null, null, null])[(floor(random() * 20) + 1)],
                           array_to_string(
                                   array [(array ['Мясокомбинат', 'Молокозавод', 'Швейная фабрика', 'Завод металлических конструкций', 'Кабельный завод', 'Судоремонтный завод', 'Производственной объединение', 'Завод электромеханический', 'Станкостроительный завод', 'Производство пластиковых изделий', 'Консервный завод', 'Птицеферма'])[(floor(random() * 12) + 1)],
                                       array_to_string(
                                               array [(array ['№ 1', '№ 2', '№ 3', '№ 4', '№ 5', '№ 6', '№ 7', '№ 8', '№ 9', '№ 10', '№ 11', '№ 12', null, null, null, null, null, null])[(floor(random() * 18) + 1)],
                                                   (array ['имени Микояна', 'имени Ленина', 'имени Кирова', 'имени Свердлова', 'Московский', 'Санкт-Петербургский', 'Солекамский', 'имени 29 февраля', 'имени Пушкина', 'на Зеленой', 'на Красной', 'на Синей', null, null, null, null, null, null])[(floor(random() * 18) + 1)]],
                                               ' ')], ' '))
                       || ', ' ||
                   (array ['Бухгалтерия', 'Транспортный отдел', 'Логистический отдел', 'Отдел продаж', 'Отдел закупок', 'Отдел проектирования', 'Сборочный цех', 'Производственный цех', 'Отдел кадров', 'Отдел маркетинга', 'Юридический отдел', 'Отдел эксплуатации'])[(floor(random() * 12) + 1)] as short_name
            from generate_series(1, (select count(*) from subdivision))) z on true
      order by z.id, z.short_name) f
WHERE (SELECT count(*) FROM subdivision) < 300;

insert into subdivision(id, short_name, path, parent_id)
select f.id,
       f.short_name,
       array_to_string(array [(select s2.path from subdivision s2 where s2.id = f.parent), f.id::varchar], ' '),
       f.parent
from (select distinct on (z.id) p.id as parent, z.id, z.short_name
      from (select s.id from subdivision s order by random() limit 100) p
               left join
           (select gen_random_uuid()                                                                                                                                                                                                                                                                 as id,
                   coalesce(
                           (array ['ООО "Василёк"', 'ООО "Кофейня"', 'ООО "Вторчермет"', 'ООО "Альдебаран"', 'ООО "СтройВсем"', 'ООО "Рога и Копыта"', 'ООО "Сидоров и Ко"', 'ООО "Эквилибрист"', 'ООО "Мастер"', 'ООО "Субмарина"', 'ООО "Здоровье"', 'ООО "Анжелика"', null, null, null, null, null, null, null, null, null, null, null, null])[(floor(random() * 20) + 1)],
                           array_to_string(
                                   array [(array ['Мясокомбинат', 'Молокозавод', 'Швейная фабрика', 'Завод металлических конструкций', 'Кабельный завод', 'Судоремонтный завод', 'Производственной объединение', 'Завод электромеханический', 'Станкостроительный завод', 'Производство пластиковых изделий', 'Консервный завод', 'Птицеферма'])[(floor(random() * 12) + 1)],
                                       array_to_string(
                                               array [(array ['№ 1', '№ 2', '№ 3', '№ 4', '№ 5', '№ 6', '№ 7', '№ 8', '№ 9', '№ 10', '№ 11', '№ 12', null, null, null, null, null, null])[(floor(random() * 18) + 1)],
                                                   (array ['имени Микояна', 'имени Ленина', 'имени Кирова', 'имени Свердлова', 'Московский', 'Санкт-Петербургский', 'Солекамский', 'имени 29 февраля', 'имени Пушкина', 'на Зеленой', 'на Красной', 'на Синей', null, null, null, null, null, null])[(floor(random() * 18) + 1)]],
                                               ' ')], ' '))
                       || ', ' ||
                   (array ['Бухгалтерия', 'Транспортный отдел', 'Логистический отдел', 'Отдел продаж', 'Отдел закупок', 'Отдел проектирования', 'Сборочный цех', 'Производственный цех', 'Отдел кадров', 'Отдел маркетинга', 'Юридический отдел', 'Отдел эксплуатации'])[(floor(random() * 12) + 1)] as short_name
            from generate_series(1, (select count(*) from subdivision))) z on true
      order by z.id, z.short_name) f
WHERE (SELECT count(*) FROM subdivision) < 300;

insert into subdivision(id, short_name, path, parent_id)
select f.id,
       f.short_name,
       array_to_string(array [(select s2.path from subdivision s2 where s2.id = f.parent), f.id::varchar], ' '),
       f.parent
from (select distinct on (z.id) p.id as parent, z.id, z.short_name
      from (select s.id from subdivision s order by random() limit 100) p
               left join
           (select gen_random_uuid()                                                                                                                                                                                                                                                                 as id,
                   coalesce(
                           (array ['ООО "Василёк"', 'ООО "Кофейня"', 'ООО "Вторчермет"', 'ООО "Альдебаран"', 'ООО "СтройВсем"', 'ООО "Рога и Копыта"', 'ООО "Сидоров и Ко"', 'ООО "Эквилибрист"', 'ООО "Мастер"', 'ООО "Субмарина"', 'ООО "Здоровье"', 'ООО "Анжелика"', null, null, null, null, null, null, null, null, null, null, null, null])[(floor(random() * 20) + 1)],
                           array_to_string(
                                   array [(array ['Мясокомбинат', 'Молокозавод', 'Швейная фабрика', 'Завод металлических конструкций', 'Кабельный завод', 'Судоремонтный завод', 'Производственной объединение', 'Завод электромеханический', 'Станкостроительный завод', 'Производство пластиковых изделий', 'Консервный завод', 'Птицеферма'])[(floor(random() * 12) + 1)],
                                       array_to_string(
                                               array [(array ['№ 1', '№ 2', '№ 3', '№ 4', '№ 5', '№ 6', '№ 7', '№ 8', '№ 9', '№ 10', '№ 11', '№ 12', null, null, null, null, null, null])[(floor(random() * 18) + 1)],
                                                   (array ['имени Микояна', 'имени Ленина', 'имени Кирова', 'имени Свердлова', 'Московский', 'Санкт-Петербургский', 'Солекамский', 'имени 29 февраля', 'имени Пушкина', 'на Зеленой', 'на Красной', 'на Синей', null, null, null, null, null, null])[(floor(random() * 18) + 1)]],
                                               ' ')], ' '))
                       || ', ' ||
                   (array ['Бухгалтерия', 'Транспортный отдел', 'Логистический отдел', 'Отдел продаж', 'Отдел закупок', 'Отдел проектирования', 'Сборочный цех', 'Производственный цех', 'Отдел кадров', 'Отдел маркетинга', 'Юридический отдел', 'Отдел эксплуатации'])[(floor(random() * 12) + 1)] as short_name
            from generate_series(1, (select count(*) from subdivision))) z on true
      order by z.id, z.short_name) f
WHERE (SELECT count(*) FROM subdivision) < 300;


insert into users(id, subdivision_id, last_name, first_name, middle_name)
select gen_random_uuid(),
       (select id from subdivision where g > 0 order by random() limit 1),
       (array ['Зайцев', 'Волков', 'Медведев', 'Собакин', 'Кошкин', 'Куницын', 'Мышкевич', 'Лосев', 'Сорокин', 'Журавлев', 'Сазонов', 'Карпов'])[(floor(random() * 12) + 1)]                               as last_name,
       (array ['Иван', 'Михаил', 'Сергей', 'Петр', 'Артем', 'Евгений', 'Даниил', 'Александр', 'Денис', 'Василий', 'Григорий', 'Илья'])[(floor(random() * 12) + 1)]                                         as first_name,
       (array ['Иванович', 'Михаилович', 'Сергеевич', 'Петрович', 'Артемович', 'Евгеньевич', 'Данилович', 'Александрович', 'Денисович', 'Васильевич', 'Григорьевич', 'Ильич'])[(floor(random() * 12) + 1)] as middle_name
from generate_series(1, 10000) g
where (select count(*) from users) < 100;

insert into user_to_position(user_id, position_id)
select z.id, unnest(z.pos)
from (select u.id,
             (select array_agg(b.id)
              from (select p.id
                    from position p
                    where u.id is not null
                    order by random()
                    limit floor(random() * 3 + 1)) b) as pos
      from users u
      group by u.id
      order by u.id
      offset 100 limit 100) z
where (select count(*) from user_to_position) < 100;
