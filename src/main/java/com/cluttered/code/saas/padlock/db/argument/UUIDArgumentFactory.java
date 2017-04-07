package com.cluttered.code.saas.padlock.db.argument;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
public class UUIDArgumentFactory implements ArgumentFactory<UUID> {

    @Override
    public boolean accepts(final Class<?> expectedType, final Object value, final StatementContext ctx) {
        return value instanceof UUID;
    }

    @Override
    public Argument build(final Class<?> expectedType, final UUID value, final StatementContext ctx) {
        return new UUIDArgument(value);
    }

    private static class UUIDArgument implements Argument {
        private final UUID value;

        public UUIDArgument(final UUID value) {
            this.value = value;
        }

        @Override
        public void apply(final int position, final PreparedStatement statement, final StatementContext ctx) throws SQLException {
            statement.setString(position, value.toString());
        }
    }
}