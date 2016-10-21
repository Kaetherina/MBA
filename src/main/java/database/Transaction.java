package database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.UserTransaction;

/**
 * Created by vrettos on 14.10.2016.
 */
@ApplicationScoped
public class Transaction {
    private static final Logger log = LoggerFactory.getLogger(Transaction.class);

    @Resource
    UserTransaction userTransaction;

    public void begin() {
        try {
            userTransaction.begin();
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        try {
            userTransaction.commit();
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new RuntimeException(e);
        }
    }
}