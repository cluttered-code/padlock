package com.cluttered.code.saas.padlock.db.dao;

import com.cluttered.code.saas.padlock.db.dto.PasswordDto;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
@RegisterMapper(PasswordDto.Mapper.class)
public interface PasswordDao {

    @SqlUpdate("INSERT INTO passwords (user_id, hash) VALUES (:userId, :hash)")
    Integer insert(@BindBean final PasswordDto password);

    @SqlQuery("SELECT * FROM passwords WHERE user_id = :userId")
    PasswordDto findByUserId(@Bind("userId") final UUID userId);

    @SqlUpdate("DELETE FROM passwords WHERE user_id = :userId")
    Integer deleteByUserId(@Bind("userId") final UUID userId);
}
