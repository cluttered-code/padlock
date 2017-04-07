package com.cluttered.code.saas.padlock.db.dao;

import com.cluttered.code.saas.padlock.db.dto.UserDto;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Collection;
import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
@RegisterMapper(UserDto.Mapper.class)
public interface UserDao {

    @SqlQuery("SELECT * FROM users")
    Collection<UserDto> listAll();

    @SqlUpdate("INSERT INTO users (id, email) VALUES (:id, :email)")
    Integer insert(@BindBean final UserDto user);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    UserDto findById(@Bind("id") final UUID id);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    UserDto findByEmail(@Bind("email") final String email);

    @SqlUpdate("UPDATE users SET email = :email WHERE id = :id")
    Integer update(@BindBean final UserDto user);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    Integer deleteById(@Bind("id") final UUID id);
}
