package database.daos;

import domain.Gll;
import domain.UuidId;
import org.apache.log4j.spi.LoggerFactory;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by vrettos on 14.10.2016.
 * Used for Storing Gll Data in the Gll Table of the Database
 */
public class GllDao implements Dao<String, Gll>  {
    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GllDao.class);

    private boolean inTest = false;

    private Connection getConnection(){
        if(inTest){
            return entityManager.unwrap(SessionImpl.class).connection();
        }else{
            return entityManager.unwrap(Connection.class);
        }

    }

    @Override
    public void persist(Gll entity){
        Connection con = getConnection();
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement("insert into gll(id, vin, ts, latitude, longitude, altitude) values (?, ?,  ?, ?, ?, ?)");
            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getVin());
            statement.setString(3, entity.getTimestamp());
            statement.setDouble(4, entity.getLatitude());
            statement.setDouble(5, entity.getLongitude());
            statement.setDouble(6, entity.getAltitude());
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

    @Override
    public Collection<Gll> list() {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<Gll> datalist = new ArrayList<>();

        try {
            statement = con.prepareStatement("select id, vin, ts, latitude, longitude, altitude from gll ORDER BY vin, ts");
            result = statement.executeQuery();

            if (!result.next()) {
                System.out.println("nothing in the database!");
                return null;
            }
            System.out.println("output from the database:");
            do {
                Gll data = new Gll();
                data.setId(result.getString(1));
                data.setVin(result.getString(2));
                data.setTimestamp(result.getString(3));
                data.setLatitude(result.getDouble(4));
                data.setLongitude(result.getDouble(5));
                data.setAltitude(result.getDouble(6));

                datalist.add(data);
            }while(result.next());
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
        return datalist;
    }

    public Gll lastGllbyVin(String vin) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        Gll Data;
        try {
            statement = con.prepareStatement("select id, vin, ts, latitude, longitude, altitude from gll where vin = ? ORDER BY ts DESC LIMIT 1");
            statement.setString(1, vin);
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }

            Data = new Gll();
            Data.setId(result.getString(1));
            Data.setVin(result.getString(2));
            Data.setTimestamp(result.getString(3));
            Data.setLatitude(result.getDouble(4));
            Data.setLongitude(result.getDouble(5));
            Data.setAltitude(result.getDouble(6));

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

    @Override
    public Gll get(String gllId) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        Gll Data;
        try {
            statement = con.prepareStatement("select id, vin, ts, latitude, longitude, altitude from gll where id = ?");
            statement.setString(1, gllId);
            result = statement.executeQuery();
            if (!result.next()) {
                System.out.println("didn't find row in Gll with id " + gllId);
                return null;
            }
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }

            Data = new Gll();
            Data.setId(gllId);
            Data.setVin(result.getString(2));
            Data.setTimestamp(result.getString(3));
            Data.setLatitude(result.getDouble(4));
            Data.setLongitude(result.getDouble(5));
            Data.setAltitude(result.getDouble(6));

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

    @Override
    public void delete(String gllId){
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("delete from gll where id = ?");
            statement.setString(1, gllId);
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

    public void dropGll(){
        Connection con = getConnection();
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement("drop table Gll");
            statement.executeUpdate();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Could not delete Table Gll: " + e);
        }finally{
            try{
                if(statement != null && !statement.isClosed())
                    statement.close();
            }catch(SQLException e){
                //ignore
            }
        }
    }
}
