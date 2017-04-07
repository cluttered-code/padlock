package com.cluttered.code.saas.padlock.db.dto;

import com.cluttered.code.saas.padlock.model.Credential;
import com.cluttered.code.saas.padlock.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
public class UserDto {

    private static final String ID = "id";
    private static final String EMAIL = "email";

    private final UUID id;
    private final String email;

    private UserDto(final UUID id, final String email) {
        this.id = id;
        this.email = email;
    }

    public static UserDto fromCredential(final Credential credential) {
        return new UserDto(UUID.randomUUID(), credential.getEmail());
    }

    public static UserDto fromUser(final User user) {
        return new UserDto(user.getId(), user.getEmail());
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public static class Mapper implements ResultSetMapper<UserDto> {
        @Override
        public UserDto map(final int index, final ResultSet rs, final StatementContext ctx) throws SQLException {
            return new UserDto(
                    (UUID) rs.getObject(ID),
                    rs.getString(EMAIL)
            );
        }
    }
}
