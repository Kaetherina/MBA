package rest;

import database.daos.GllDao;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.sql.SQLException;

/**
 * Created by vrettos on 14.10.2016.
 */
@Singleton
@Startup
public class StartUpBean {
    private static final Logger log = LoggerFactory.getLogger(StartUpBean.class);
    public static final String DB_PORT = "9092";

    @Inject
    GllDao gllDao;

    @PostConstruct
    public void init(){
        log.info("Message Broker Application Database started");
        startDbServer();
    }

    @PreDestroy
    public void shutdown() {
        stopDbServer();
    }

    /**
     * Start H2 db as server.
     * You can connect remotly using this URL:
     * jdbc:h2:tcp://localhost:9092/~/MbaDB
     * User: sa
     * Pwd: <KEEP EMPTY>
     *
     * WARNING: Server is NOT secured. Don't use in production!!!!!
     */
    private void startDbServer() {
        try {
            Server.createTcpServer("-tcpPort", DB_PORT, "-tcpAllowOthers").start();
            log.warn("WARNING: H2 Server started. Only for testing allowed! Don't start on production system!!!!!");
        } catch (SQLException e) {
            log.error("Could not start db server: " + e);
        }

    }


    private static void stopDbServer() {
        try {
            Server.shutdownTcpServer("tcp://localhost:" + DB_PORT, "", true, true);
        } catch (SQLException e) {
            log.error("Could not shutdown db server: " + e);
        }
    }

}
