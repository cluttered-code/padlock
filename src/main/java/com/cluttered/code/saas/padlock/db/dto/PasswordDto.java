package com.cluttered.code.saas.padlock.db.dto;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
public class PasswordDto {

    private static final String USER_ID = "user_id";
    private static final String HASH = "hash";

    private final UUID userId;
    private final byte[] hash;

    public PasswordDto(final UUID userId, final byte[] hash) {
        this.userId = userId;
        this.hash = hash;
    }

    public UUID getUserId() {
        return userId;
    }

    public byte[] getHash() {
        return hash;
    }

    public static class Mapper implements ResultSetMapper<PasswordDto> {
        @Override
        public PasswordDto map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException {
            return new PasswordDto(
                    (UUID) rs.getObject(USER_ID),
                    rs.getBytes(HASH)
            );
        }
    }
}
