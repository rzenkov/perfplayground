/*
 * This file is generated by jOOQ.
 */
package ru.rzen.perflayground.jooq.tables;


import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import ru.rzen.perflayground.jooq.Keys;
import ru.rzen.perflayground.jooq.Public;
import ru.rzen.perflayground.jooq.tables.Users.UsersPath;
import ru.rzen.perflayground.jooq.tables.records.SubdivisionRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Subdivision extends TableImpl<SubdivisionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.subdivision</code>
     */
    public static final Subdivision SUBDIVISION = new Subdivision();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SubdivisionRecord> getRecordType() {
        return SubdivisionRecord.class;
    }

    /**
     * The column <code>public.subdivision.id</code>.
     */
    public final TableField<SubdivisionRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.subdivision.short_name</code>.
     */
    public final TableField<SubdivisionRecord, String> SHORT_NAME = createField(DSL.name("short_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.subdivision.path</code>.
     */
    public final TableField<SubdivisionRecord, String> PATH = createField(DSL.name("path"), SQLDataType.VARCHAR(10000), this, "");

    /**
     * The column <code>public.subdivision.parent_id</code>.
     */
    public final TableField<SubdivisionRecord, UUID> PARENT_ID = createField(DSL.name("parent_id"), SQLDataType.UUID, this, "");

    private Subdivision(Name alias, Table<SubdivisionRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Subdivision(Name alias, Table<SubdivisionRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.subdivision</code> table reference
     */
    public Subdivision(String alias) {
        this(DSL.name(alias), SUBDIVISION);
    }

    /**
     * Create an aliased <code>public.subdivision</code> table reference
     */
    public Subdivision(Name alias) {
        this(alias, SUBDIVISION);
    }

    /**
     * Create a <code>public.subdivision</code> table reference
     */
    public Subdivision() {
        this(DSL.name("subdivision"), null);
    }

    public <O extends Record> Subdivision(Table<O> path, ForeignKey<O, SubdivisionRecord> childPath, InverseForeignKey<O, SubdivisionRecord> parentPath) {
        super(path, childPath, parentPath, SUBDIVISION);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class SubdivisionPath extends Subdivision implements Path<SubdivisionRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> SubdivisionPath(Table<O> path, ForeignKey<O, SubdivisionRecord> childPath, InverseForeignKey<O, SubdivisionRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private SubdivisionPath(Name alias, Table<SubdivisionRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public SubdivisionPath as(String alias) {
            return new SubdivisionPath(DSL.name(alias), this);
        }

        @Override
        public SubdivisionPath as(Name alias) {
            return new SubdivisionPath(alias, this);
        }

        @Override
        public SubdivisionPath as(Table<?> alias) {
            return new SubdivisionPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<SubdivisionRecord> getPrimaryKey() {
        return Keys.SUBDIVISION_PKEY;
    }

    private transient UsersPath _users;

    /**
     * Get the implicit to-many join path to the <code>public.users</code> table
     */
    public UsersPath users() {
        if (_users == null)
            _users = new UsersPath(this, null, Keys.USERS__FK_USERS_ON_SUBDIVISION.getInverseKey());

        return _users;
    }

    @Override
    public Subdivision as(String alias) {
        return new Subdivision(DSL.name(alias), this);
    }

    @Override
    public Subdivision as(Name alias) {
        return new Subdivision(alias, this);
    }

    @Override
    public Subdivision as(Table<?> alias) {
        return new Subdivision(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Subdivision rename(String name) {
        return new Subdivision(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Subdivision rename(Name name) {
        return new Subdivision(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Subdivision rename(Table<?> name) {
        return new Subdivision(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Subdivision where(Condition condition) {
        return new Subdivision(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Subdivision where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Subdivision where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Subdivision where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Subdivision where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Subdivision where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Subdivision where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Subdivision where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Subdivision whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Subdivision whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
