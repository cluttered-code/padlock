package com.cluttered.code.saas.padlock.db;

import com.cluttered.code.saas.padlock.db.dao.UserDao;
import com.cluttered.code.saas.padlock.db.dto.UserDto;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import java.util.Collection;
import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
@Singleton
public class UserRepository {

    final UserDao dao;

    @Inject
    private UserRepository(final UserDao dao) {
        this.dao = dao;
    }

    public Promise<Collection<UserDto>> listAll() {
        return Blocking.get(dao::listAll);
    }

    public Promise<UserDto> insert(final UserDto dto) {
        return Blocking.get(() -> dao.insert(dto))
                .map(count -> dto);
    }

    public Promise<UserDto> findById(final UUID id) {
        return Blocking.get(() -> dao.findById(id));
    }

    public Promise<UserDto> findByEmail(final String email) {
        return Blocking.get(() -> dao.findByEmail(email));
    }

    public Promise<UserDto> update(final UserDto dto) {
        return Blocking.get(() -> dao.update(dto))
                .map(count -> dto);
    }

    public Promise<Integer> deleteById(final UUID id) {
        return Blocking.get(() -> dao.deleteById(id));
    }
}
