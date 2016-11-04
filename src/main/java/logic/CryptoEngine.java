package logic;

/**
 * Created by vrettos on 03.11.2016.
 * Still all in red until voltage Library is imported and used
 */
import com.voltage.securedata.enterprise.FPE;
import com.voltage.securedata.enterprise.LibraryContext;
import com.voltage.securedata.enterprise.VeException;

import java.util.Objects;


public class CryptoEngine{

    private static final String LIBRARY_NAME;
    private static final String policyURL;
    private static final String identity;
    private static final String sharedSecret;
    private static final String trustStorePath;
    private static final String cachePath;
    private static final String[] formats;
    private static final FPE[] fpes;

    static{
        PropertiesReader prop = new PropertiesReader("/home/demo/Documents/eclipse_j2ee/workspace/EPA/resources/voltage.properties");

        LIBRARY_NAME = prop.getLIBRARY_NAME();
        policyURL = prop.getPolicyURL();
        identity = prop.getIdentity();
        sharedSecret = prop.getSharedSecret();
        trustStorePath = prop.getTrustStorePath();
        cachePath = prop.getCachePath();
        formats = prop.getFormats();
        int l = formats.length;
        //	TODO: Check the multi-threading behaviour of these objects, as well as object creation time.
        LibraryContext library       = null;
        fpes = new FPE[l];

//    	Load the JNI library
        System.out.print("Current value of java.library.path: " + System.getProperty("java.library.path")+ " running on "+ System.getProperty("os.name"));
        System.out.print("Loading library " + LIBRARY_NAME + " ... ");
        System.loadLibrary(LIBRARY_NAME);
        System.out.println("success!");

//    	Dev Guide Code Snippet: VERSION; LC:2
        System.out.println("SimpleAPI version: " + LibraryContext.getVersion());
        System.out.println();

        try{
//				 Dev Guide Code Snippet: LIBCTXBUILD; LC:6  Create the context for crypto operations
            System.out.print("Constructing library object... ");
            library = new LibraryContext.Builder()
                    .setPolicyURL(policyURL)
                    .setFileCachePath(cachePath)
                    .setTrustStorePath(trustStorePath)
                    .build();
            System.out.println("success!");

            for (int i=0; i<l; i++){
                if(!Objects.equals(formats[i], "") && formats[i]!=null){
                    // 	Dev Guide Code Snippet: FPEBUILD; LC:5
                    System.out.print("Constructing "+ formats[i] + " fpe object... ");
                    fpes[i] = library.getFPEBuilder(formats[i])
                            .setSharedSecret(sharedSecret)
                            .setIdentity(identity)
                            .build();
                    System.out.println("success!");
                } else{
                    System.out.println("The " + (i+1) + ". format value is empty");
                }
            }
        }catch(Throwable ex){
            System.err.println("Failed: Unexpected exception" + ex);
            ex.printStackTrace();
        }

    }

    public String encrypt (String plainText, String format){
        FPE fpe = null;

        for(int i = 0; i<formats.length; i++){
            if(formats[i].equals(format)){
                fpe = fpes[i];
                //System.out.println("format " + format + " found!");
                break;
            }
            if(i == formats.length-1){
                System.out.println("format " + format + " not found in array formats, check voltage properties for correct spelling.");
            }
        }

        if(fpe!=null) {
            synchronized (fpe) {
                try {
                    return fpe.protect(plainText);
                } catch (VeException ex) {
                    System.err.println("Error during Voltage encryption");
                    ex.printStackTrace();
                    return "***FAILED***";
                }
            }
        }else{
            System.out.println("fpe undefined - error was thrown earlier");
            return "***FAILED***";
        }
    }

    public String decrypt (String encrText, String format) {
        FPE fpe = null;

        for(int i = 0; i<formats.length; i++){
            if(formats[i].equals(format)){
                fpe = fpes[i];
                //System.out.println("format " + format + " found!");
                break;
            }
            if(i == formats.length-1){
                System.out.println("format " + format + " not found in array formats, check voltage properties for correct spelling.");
            }
        }

        if(fpe!=null) {
            synchronized (fpe) {
                try {
                    return fpe.access(encrText);
                } catch (VeException ex) {
                    System.err.println("Error during Voltage decryption");
                    ex.printStackTrace();
                    return "***FAILED***";
                }
            }
        }else{
            System.out.println("fpe undefined - error was thrown earlier");
            return "***FAILED***";
        }
    }

    public String encrypt (String plainText){
        FPE fpe = null;
        String format = "AUTO";

        for(int i = 0; i<formats.length; i++){
            if(formats[i].equals(format)){
                fpe = fpes[i];
                //System.out.println("format " + format + " found!");
                break;
            }
            if(i == formats.length-1){
                System.out.println("format " + format + " not found in array formats, check voltage properties for correct spelling.");
            }
        }

        if(fpe!=null) {
            synchronized (fpe) {
                try {
                    return fpe.protect(plainText);
                } catch (VeException ex) {
                    System.err.println("Error during Voltage encryption");
                    ex.printStackTrace();
                    return "***FAILED***";
                }
            }
        }else{
            System.out.println("fpe undefined - error was thrown earlier");
            return "***FAILED***";
        }
    }

    public String decrypt (String plainText){
        FPE fpe = null;
        String format = "AUTO";

        for(int i = 0; i<formats.length; i++){
            if(formats[i].equals(format)){
                fpe = fpes[i];
                //System.out.println("format " + format + " found!");
                break;
            }
            if(i == formats.length-1){
                System.out.println("format " + format + " not found in the formats array, check voltage properties for correct spelling.");
            }
        }

        if(fpe!=null) {
            synchronized (fpe) {
                try {
                    return fpe.access(plainText);
                } catch (VeException ex) {
                    System.err.println("Error during Voltage decryption");
                    ex.printStackTrace();
                    return "***FAILED***";
                }
            }
        }else{
            System.out.println("fpe undefined - error was thrown earlier");
            return "***FAILED***";
        }
    }

}

