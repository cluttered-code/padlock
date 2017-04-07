package com.cluttered.code.saas.padlock;

import com.cluttered.code.saas.padlock.action.LoginActionChain;
import com.cluttered.code.saas.padlock.action.UsersActionChain;
import com.cluttered.code.saas.padlock.db.UserRepository;
import com.cluttered.code.saas.padlock.db.argument.UUIDArgumentFactory;
import com.cluttered.code.saas.padlock.db.dao.PasswordDao;
import com.cluttered.code.saas.padlock.db.dao.UserDao;
import com.cluttered.code.saas.padlock.handler.CORSHandler;
import com.cluttered.code.saas.padlock.service.LiquibaseService;
import com.cluttered.code.saas.padlock.service.LoggingService;
import com.cluttered.code.saas.padlock.service.PasswordService;
import com.cluttered.code.saas.padlock.service.UserService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.skife.jdbi.v2.DBI;

import javax.sql.DataSource;

/**
 * @author cluttered.code@gmail.com
 */
public class PadlockModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CORSHandler.class);

        bind(UsersActionChain.class);
        bind(LoginActionChain.class);

        bind(LiquibaseService.class);
        bind(LoggingService.class);
        bind(UserService.class);
        bind(PasswordService.class);

        bind(UserRepository.class);
    }

    @Provides
    @Singleton
    public DBI providesDBI(final DataSource dataSource) {
        final DBI jdbi = new DBI(dataSource);
        jdbi.registerArgumentFactory(new UUIDArgumentFactory());
        return jdbi;
    }

    @Provides
    @Singleton
    public UserDao providesUserDao(final DBI jdbi) {
        return jdbi.onDemand(UserDao.class);
    }

    @Provides
    @Singleton
    public PasswordDao providesPasswordDao(final DBI jdbi) {
        return jdbi.onDemand(PasswordDao.class);
    }
}
