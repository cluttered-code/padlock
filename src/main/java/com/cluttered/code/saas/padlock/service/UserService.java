package com.cluttered.code.saas.padlock.service;

import com.cluttered.code.saas.padlock.db.UserRepository;
import com.cluttered.code.saas.padlock.db.dto.UserDto;
import com.cluttered.code.saas.padlock.model.Credential;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.service.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * @author cluttered.code@gmail.com
 */
@Singleton
public class UserService implements Service {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    final UserRepository userRepository;

    @Inject
    private UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Promise<Collection<UserDto>> listAll() {
        LOG.warn("listing all users");
        return userRepository.listAll();
    }

    public Promise<UserDto> insert(final Credential credential) {
        final UserDto userDto = UserDto.fromCredential(credential);
        LOG.info("inserting new user: {}", userDto.getEmail());
        return userRepository.insert(userDto);
    }

    public Promise<UserDto> findById(final UUID id) {
        LOG.trace("finding user by id: {}", id);
        return userRepository.findById(id);
    }

    public Promise<UserDto> findByCredential(final Credential credential) {
        LOG.trace("finding user by email: {}", credential.getEmail());
        return userRepository.findByEmail(credential.getEmail());
    }

    public Promise<UserDto> update(final UserDto userDto) {
        LOG.debug("updating user: {}", userDto.getId());
        return userRepository.update(userDto);
    }

    public Promise<Integer> deleteById(final UUID id) {
        LOG.info("deleting user: {}", id);
        return userRepository.deleteById(id);
    }
}
