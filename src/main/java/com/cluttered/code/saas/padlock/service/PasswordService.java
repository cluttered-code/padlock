package com.cluttered.code.saas.padlock.service;

import com.cluttered.code.saas.padlock.db.dao.PasswordDao;
import com.cluttered.code.saas.padlock.db.dto.PasswordDto;
import com.cluttered.code.saas.padlock.db.dto.UserDto;
import com.cluttered.code.saas.padlock.model.Credential;
import com.cluttered.code.saas.padlock.util.PasswordHash;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.func.Pair;
import ratpack.service.Service;

import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
@Singleton
public class PasswordService implements Service {

    private final PasswordDao passwordDao;

    @Inject
    private PasswordService(final PasswordDao passwordDao) {
        this.passwordDao = passwordDao;
    }

    public Promise<Pair<UserDto, Integer>> insert(final Pair<Credential, UserDto> credentialUserDtoPair) {
        return Blocking.get(() -> PasswordHash.hash(credentialUserDtoPair.getLeft().getPassword()))
                .map(passwordHash -> new PasswordDto(credentialUserDtoPair.getRight().getId(), passwordHash))
                .map(passwordDao::insert)
                .map(insertCount -> Pair.of(credentialUserDtoPair.getRight(), insertCount));
    }

    public Promise<Boolean> validate(final Pair<Credential, UserDto> credentialUserDtoPair) {
        // Use random uuid and empty byte array to ensure validation has consistent time on success and failure
        return Blocking.get(() -> {
            final UUID id = credentialUserDtoPair.getRight() != null ? credentialUserDtoPair.getRight().getId() : UUID.randomUUID();
            return passwordDao.findByUserId(id);
        }).map(passwordDto -> {
            final byte[] hash = passwordDto != null ? passwordDto.getHash() : new byte[0];
            return PasswordHash.validate(credentialUserDtoPair.getLeft().getPassword(), hash);
        });
    }

    public Promise<Integer> delete(final Pair<UUID, Integer> uuidIntegerPair) {
        return Blocking.get(() -> passwordDao.deleteByUserId(uuidIntegerPair.getLeft()));
    }
}
