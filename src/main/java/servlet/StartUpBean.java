package servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
/**
 * Created by vrettos on 14.10.2016.
 */
@Singleton
@Startup
public class StartUpBean {
    private static final Logger log = LoggerFactory.getLogger(StartUpBean.class);

    //@Inject Dao später

    @PostConstruct
    public void init(){
        log.info("MeetMe Server started");
        //initial code goes here
    }
}
