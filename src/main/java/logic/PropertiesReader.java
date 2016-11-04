package logic;

/**
 * Created by vrettos on 03.11.2016.
 * Take updates from the EndPointApplication
 * Used to read the voltage properties from voltage.properties
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.Reader;
import java.lang.StringBuffer;

public class PropertiesReader {

    String LIBRARY_NAME;
    String policyURL;
    String identity;
    String sharedSecret;
    String trustStorePath;
    String cachePath;
    StringBuffer sbFormats;
    String[] formats;


    public PropertiesReader(String filePath){
        InputStream ins = null; // raw byte-stream
        Reader r = null; // cooked reader
        BufferedReader br = null; // buffered for readLine()
        String s; //for each row of the document

        try {
            ins = new FileInputStream(filePath);
            r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
            br = new BufferedReader(r);

            while ((s = br.readLine()) != null) {
                if(s.charAt(0)!='#'){
                    StringBuffer sb = new StringBuffer(s);
                    String key 	= sb.substring(0, sb.indexOf("="));
                    sb.delete(0, (sb.indexOf("=")+1));
                    String value = sb.toString();

                    //System.out.println("key is: -" + key + "- and the value: -" + value +"-");

                    inner_switch:
                    switch(key){
                        case "LIBRARY_NAME": 	setLIBRARY_NAME(value);
                            break inner_switch;
                        case "policyURL":		setPolicyURL(value);
                            break inner_switch;
                        case "identity":		setIdentity(value);
                            break inner_switch;
                        case "sharedSecret":	setSharedSecret(value);
                            break inner_switch;
                        case "trustStorePath":	setTrustStorePath(value);
                            break inner_switch;
                        case "cachePath":		setCachePath(value);
                            break inner_switch;
                        case "format":			iniSetFormats(value);
                            break inner_switch;
                    }
                }
            }
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
        catch (Exception e){
            System.err.println(e.getMessage()); // handle exception
        }

        finally {
            if (r != null) {
                try { r.close(); }
                catch(Throwable t) {
                    System.err.println("Reader could not be closed: "+ t.getMessage());
                }}
            if (ins != null) {
                try { ins.close(); }
                catch(Throwable t) {
                    System.err.println("FileInputStream could not be closed: "+ t.getMessage());
                }}
        }
    }


    public void iniSetFormats(String value){
        StringBuffer sb = new StringBuffer(value);
        int l = (sb.toString().split(",").length); //how often the "," is inserted
        System.out.println("found " +l+ " formats");
        formats = new String[l]; //format as an array of formats
        for (int i=0; i<l; i++){
            if(sb.toString()!=""){
                if(i==(l-1)){
                    formats[i] = sb.toString();
                    System.out.println("inserted last format into array: "+ formats[i]);
                }else{
                    formats[i] = sb.substring(0, sb.indexOf(","));
                    sb.delete(0, (sb.indexOf(",")+1));
                    System.out.println("inserted format " + formats[i] + " into array formats, left is " + sb.toString());
                }
            }else{
                System.out.println("change voltage.properties to not have a comma at the end");
            }
        }
    }


    public String getLIBRARY_NAME() {
        return LIBRARY_NAME;
    }
    public void setLIBRARY_NAME(String lIBRARY_NAME) {
        LIBRARY_NAME = lIBRARY_NAME;
    }
    public String getPolicyURL() {
        return policyURL;
    }
    public void setPolicyURL(String policyURL) {
        this.policyURL = policyURL;
    }
    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    public String getSharedSecret() {
        return sharedSecret;
    }
    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }
    public String getTrustStorePath() {
        return trustStorePath;
    }
    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }
    public String getCachePath() {
        return cachePath;
    }
    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public String[] getFormats(){
        return formats;
    }
    public void setFormats(String[] formats){
        this.formats = formats;
    }

}

