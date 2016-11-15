package rest;

import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
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
import java.util.Collection;
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
    @Inject Transaction transaction;

    @Path("ping")
    @GET
    public String getServerTime(){
        System.out.println("RESTful Service 'MessageBrokerApplication' is running ==> ping");
        return "received ping on "+new Date().toString();
    }

    @Path("/list")
    @GET
    public String list(){
        log.debug("List GllData");
        StringBuilder sb = new StringBuilder("{\"GllData\":[");
        Collection<Gll> list = gllDao.list();
        JsonGll jg = new JsonGll();
        ObjectMapper mapper = new ObjectMapper();

        if(list==null){
            return "database empty!";
        }

        try {
            for (Gll data : list) {
                jg = data.toJson();
                String json = mapper.writeValueAsString(jg);
                System.out.println("appending json:" + json);
                sb.append(json);
                sb.append(",");
            }
        }
        catch(JsonParseException e){ e.printStackTrace();}
        catch(JsonMappingException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}

        sb.deleteCharAt(sb.length()-1);
        sb.append("]}");
        log.debug("String erstellt: " + sb);
        return sb.toString();

    }

    @Path("/json")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    @POST
    public String acceptJson(String jsonString) throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        try{
            System.out.println("1. received json String: "+jsonString);
            JsonGll jGll = mapper.readValue(jsonString, JsonGll.class);
            Gll gll = jGll.toDomain();
            System.out.println("2. Gll Data: id="+ gll.getIdStr() + " lat:" + gll.getLatitude() + " lon:" + gll.getLongitude() + " alt: " + gll.getAltitude() + " ts:" + gll.getTimestamp() );
            transaction.begin();
            System.out.print("Storing the Gll Object in the Database... ");
            gllDao.persist(gll);
            transaction.commit();
            System.out.println("success");
            System.out.println();
            System.out.print("Fetching the entered Gll Object... ");
            transaction.begin();
            Gll fetched = gllDao.get(gll.getId().asString());
            transaction.commit();
            System.out.println("success");

            System.out.println("2. Gll Data: id="+ fetched.getIdStr() + " lat:" + fetched.getLatitude() + " lon:" + fetched.getLongitude() + " alt: " + fetched.getAltitude() + " ts:" + fetched.getTimestamp() );

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


    //used to delete corrupt values
    @Path("/delete/{id}")
    @GET
    public String delete(@PathParam("id") String id){
        log.debug("delete the entry with id "+ id);
        transaction.begin();
        gllDao.delete(id);
        transaction.commit();
        log.debug("success!");
        return "done deleting the entry with id " + id;
    }


    // don*t use in production - only if ALL entered values are flawed
    @Path("/dropGll")
    @GET
    public String drop(){
        log.debug("dropping Table Gll now!");
        transaction.begin();
        gllDao.dropGll();
        transaction.commit();
        log.debug("done, best test once more with list");
        return "done dropping the table, better test with list..";
    }

}
