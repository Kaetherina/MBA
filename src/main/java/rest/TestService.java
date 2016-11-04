package rest;

import database.Transaction;
import domain.Gll;
import jsonObjects.JsonGll;
import groovy.lang.Singleton;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import database.daos.GllDao;

/**
 * Created by vrettos on 14.10.2016.
 */

@Path("/test")
@Produces({"application/json"}) // mime type
@Singleton
public class TestService {
    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    @Inject GllDao gllDao;
    @Inject
    Transaction transaction;

    @Path("ping")
    @GET
    public String getServerTime(){
        System.out.println("RESTful Service 'MessageBrokerApplication' is running ==> ping");
        return "received ping on "+new Date().toString();
    }


    @Path("/json")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    @POST
    public String acceptJson(String jsonString) throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonGll jGll = mapper.readValue(jsonString, JsonGll.class);
            Gll gll = jsonToDomain(jGll);
            // ToDo: failed part: getConnection in gllDao - reason yet unknown
            String vin = gll.getVin();
            transaction.begin();
            System.out.println("Storing the Gll Object in the Database... ");
            gllDao.persist(gll);
            transaction.commit();
            System.out.println("success");
            System.out.println();
            System.out.println("Fetching the last Gll Object... ");
            transaction.begin();
            Gll fetched = gllDao.lastGllbyVin(vin);
            transaction.commit();
            System.out.println("success! The entered and fetched vin is "+ fetched.getVin());

            mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            //String backToString = mapper.writeValueAsString(gll);
        }
        catch(JsonParseException e){ e.printStackTrace();}
        catch(JsonMappingException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}

        return "ok";

        /* to post:
        {
	    "vin" : "W0L000051T2123456",
        "latitude" : 39.9768,
        "longitude" : 116.329766666667,
        "altitude" : 774.278215223097,
        "timestamp" : "2008-05-09,08:12:31"
        }
        */
    }

    public Gll jsonToDomain(JsonGll jg){
        Gll gll = new Gll();
        gll.setVin(jg.getVin());
        gll.setTimestamp(jg.getTimestamp());
        gll.setLatitude(jg.getLatitude());
        gll.setLongitude(jg.getLongitude());
        gll.setAltitude(jg.getAltitude());
        return gll;
    }
}
