package com.cluttered.code.saas.padlock;

import com.cluttered.code.saas.padlock.action.LoginActionChain;
import com.cluttered.code.saas.padlock.action.UsersActionChain;
import com.cluttered.code.saas.padlock.config.LoggingConfig;
import com.cluttered.code.saas.padlock.handler.CORSHandler;
import com.cluttered.code.saas.padlock.util.Banner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.guice.Guice;
import ratpack.handling.RequestLogger;
import ratpack.hikari.HikariModule;
import ratpack.server.RatpackServer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author cluttered.code@gmail.com
 */
public class PadlockServer {

    private static final Logger LOG = LoggerFactory.getLogger(PadlockServer.class);

    private static Path configPath;

    public static void main(final String... args) throws Exception {
        Banner.display(LOG);
        resolveConfig(args);

        final RatpackServer server = createRatpackServer();
        server.start();
    }

    private static void resolveConfig(final String... args) {
        if (args.length == 0) {
            LOG.error("Need to specify config yaml as first argument");
            System.exit(1);
        }
        configPath = Paths.get(args[0]);
        LOG.info("Config: {}", configPath);
    }

    private static RatpackServer createRatpackServer() throws Exception {
        return RatpackServer.of(serverSpec -> serverSpec
                .serverConfig(config -> config
                        .yaml(configPath)
                        .require("/database", HikariConfig.class)
                        .require("/logging", LoggingConfig.class))
                .registryOf(r -> r
                        .add(ObjectMapper.class, new ObjectMapper().registerModule(new Jdk8Module())))
                .registry(Guice.registry(bindings -> bindings
                        .module(HikariModule.class)
                        .module(PadlockModule.class)))
                .handlers(chain -> chain
                        .all(RequestLogger.ncsa())
                        .all(CORSHandler.class)
                        .insert(LoginActionChain.class)
                        .insert(UsersActionChain.class))
        );
    }
}