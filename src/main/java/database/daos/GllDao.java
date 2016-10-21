package database.daos;

import domain.Gll;
import org.apache.log4j.spi.LoggerFactory;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vrettos on 14.10.2016.
 */
public class GllDao /*implements Dao<String, Gll>*/  {
    @PersistenceContext
    protected EntityManager entityManager;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GllDao.class);

    public boolean inTest = false;

    private Connection getConnection(){
        if(inTest){
            return entityManager.unwrap(SessionImpl.class).connection();
        }else{
            return entityManager.unwrap(Connection.class);
        }

    }

    //@Override
    public void persist(Gll entity){
        Connection con = getConnection();
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement("instert into gll(vin, ts, latitude, longitude, altitude) values (?,  ?, ?, ?, ?)");
            statement.setString(1, entity.getVin());
            statement.setString(2, entity.getTimestamp());
            statement.setDouble(3, entity.getLatitude());
            statement.setDouble(4, entity.getLongitude());
            statement.setDouble(5, entity.getAltitude());
            statement.executeUpdate();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Could not update database ", e);
        }finally {
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                //ignore
            }
        }
    }

    //@Override
    public void delete(String vin, String ts){
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("delete from gll where vin = ? and ts = ?");
            statement.setString(1, vin);
            statement.setString(2, ts);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update database", e);
        } finally {
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    //@Override
    public Gll gllGPSbyVin(String vin) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        Gll Data;
        try {
            statement = con.prepareStatement("select vin, ts, latitude, longitude, altitude from gll where vin = ? ORDER BY ts DESC LIMIT 1");
            statement.setString(1, vin);
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }

            Data = new Gll();
            Data.setVin(result.getString(1));
            Data.setTimestamp(result.getString(2));
            Data.setLatitude(result.getDouble(3));
            Data.setLongitude(result.getDouble(4));
            Data.setAltitude(result.getDouble(5));

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update database", e);
        } finally {
            try {
                if (result != null && !result.isClosed())
                    result.close();
            } catch (SQLException e) {
                // ignore
            }
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        return Data;
    }

}
