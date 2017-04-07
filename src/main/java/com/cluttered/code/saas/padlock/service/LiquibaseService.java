package com.cluttered.code.saas.padlock.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.service.Service;
import ratpack.service.StartEvent;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author cluttered.code@gmail.com
 */
@Singleton
public class LiquibaseService implements Service {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseService.class);

    private final DataSource dataSource;

    @Inject
    private LiquibaseService(final DataSource datasource) {
        this.dataSource = datasource;
    }

    @Override
    public void onStart(final StartEvent event) {
        migrate();
    }

    private void migrate() {
        try (final Connection connection = dataSource.getConnection()) {
            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            final Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (final SQLException | LiquibaseException ex) {
            LOG.error("Unable to migrate database", ex);
        }
    }
}
