package hu.bme.mit.cps.smartuniversity.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;

import hu.bme.mit.cps.smartuniversity.EnvironmentTemperature;
import hu.bme.mit.cps.smartuniversity.EnvironmentTemperatureDataWriter;
import hu.bme.mit.cps.smartuniversity.EnvironmentTemperatureTypeSupport;

public class WeatherForecast {
		
	    // -----------------------------------------------------------------------
	    // Public Methods
	    // -----------------------------------------------------------------------

	    public static void main(String[] args) throws ParseException {

	        // --- Get domain ID --- //
	        int domainId = 0;
	        /*if (args.length >= 1) {
	            domainId = Integer.valueOf(args[0]).intValue();
	        }*/

	        // -- Get max loop count; 0 means infinite loop --- //
	        int sampleCount = 0;
	        /*if (args.length >= 2) {
	            sampleCount = Integer.valueOf(args[1]).intValue();
	        }*/

	        /* Uncomment this to turn on additional logging
	        Logger.get_instance().set_verbosity_by_category(
	            LogCategory.NDDS_CONFIG_LOG_CATEGORY_API,
	            LogVerbosity.NDDS_CONFIG_LOG_VERBOSITY_STATUS_ALL);
	        */

	        // --- Run --- //
	        publisherMain(domainId, sampleCount);
	    }

	    // -----------------------------------------------------------------------
	    // Private Methods
	    // -----------------------------------------------------------------------

	    // --- Constructors: -----------------------------------------------------

	    private WeatherForecast() {
	        super();
	    }

	    // -----------------------------------------------------------------------

	    private static void publisherMain(int domainId, int sampleCount) throws ParseException {

	        DomainParticipant participant = null;
	        Publisher publisher = null;
	        Topic topic = null;
	        EnvironmentTemperatureDataWriter writer = null;

	        Map<String, Float> data = new HashMap<String,Float>();
	        
	        System.out.println("Reading resource file");
	        
	        try {

	            File f = new File("./weather.txt");

	            BufferedReader b = new BufferedReader(new FileReader(f));

	            String readLine = "";

	            while ((readLine = b.readLine()) != null) {        	
	            	String[] seapratedData = readLine.split(":");    
	            	System.out.println(seapratedData[0]);
	                data.put(seapratedData[0], Float.parseFloat(seapratedData[1]));
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        System.out.println("Starting WeatherPublisher");
	        try {
	            // --- Create participant --- //

	            /* To customize participant QoS, use
	            the configuration file
	            USER_QOS_PROFILES.xml */
	        	
	            participant = DomainParticipantFactory.TheParticipantFactory.
	            create_participant(
	                domainId, DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
	                null /* listener */, StatusKind.STATUS_MASK_NONE);
	            if (participant == null) {
	                System.err.println("create_participant error\n");
	                return;
	            }        

	            // --- Create publisher --- //

	            /* To customize publisher QoS, use
	            the configuration file USER_QOS_PROFILES.xml */
	            publisher = participant.create_publisher(
	                DomainParticipant.PUBLISHER_QOS_DEFAULT, null /* listener */,
	                StatusKind.STATUS_MASK_NONE);
	            if (publisher == null) {
	                System.err.println("create_publisher error\n");
	                return;
	            }                   

	            // --- Create topic --- //

	            /* Register type before creating topic */
	            String typeName = EnvironmentTemperatureTypeSupport.get_type_name();
	            EnvironmentTemperatureTypeSupport.register_type(participant, typeName);

	            /* To customize topic QoS, use
	            the configuration file USER_QOS_PROFILES.xml */

	            topic = participant.create_topic(
	                "WeatherTopic",
	                typeName, DomainParticipant.TOPIC_QOS_DEFAULT,
	                null /* listener */, StatusKind.STATUS_MASK_NONE);
	            if (topic == null) {
	                System.err.println("create_topic error\n");
	                return;
	            }           

	            // --- Create writer --- //

	            /* To customize data writer QoS, use
	            the configuration file USER_QOS_PROFILES.xml */

	            writer = (EnvironmentTemperatureDataWriter)
	            publisher.create_datawriter(
	                topic, Publisher.DATAWRITER_QOS_DEFAULT,
	                null /* listener */, StatusKind.STATUS_MASK_NONE);
	            if (writer == null) {
	                System.err.println("create_datawriter error\n");
	                return;
	            }           

	            // --- Write --- //

	            /* Create data sample for writing */
	            EnvironmentTemperature instance = new EnvironmentTemperature();
	            
	            Calendar calendar = Calendar.getInstance();
	            calendar.add(Calendar.DAY_OF_YEAR, 1);
	            
	             
	            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	            
	            InstanceHandle_t instance_handle = InstanceHandle_t.HANDLE_NIL;
	            /* For a data type that has a key, if the same instance is going to be
	            written multiple times, initialize the key here
	            and register the keyed instance prior to writing */
	            //instance_handle = writer.register_instance(instance);

	            final long sendPeriodMillis = 30 * 1000; // 4 seconds

	            int n = 0;
	            
	            for (int count = 0;
	            (sampleCount == 0) || (count < sampleCount);
	            ++count) {
	            	
	            	
	            	
	                /* Modify the instance to be written here */
	            	System.out.println(formatter.format(calendar.getTime()));
	            	
	                instance.TValue = data.get(formatter.format(calendar.getTime())); //TODO NULL
	                instance.TimeStamp = calendar.getInstance().getTimeInMillis();
	                
	                n++;
	                
	                if(n == data.size()) {
	                	n = 0;
	                }
	                
	                System.out.println("Writing Forecast" + instance.toString());
	                
	                /* Write data */
	                writer.write(instance, instance_handle);
	                try {
	                    Thread.sleep(sendPeriodMillis);
	                } catch (InterruptedException ix) {
	                    System.err.println("INTERRUPTED");
	                    break;
	                }
	            }

	            //writer.unregister_instance(instance, instance_handle);

	        } finally {

	            // --- Shutdown --- //

	            if(participant != null) {
	                participant.delete_contained_entities();

	                DomainParticipantFactory.TheParticipantFactory.
	                delete_participant(participant);
	            }
	            /* RTI Data Distribution Service provides finalize_instance()
	            method for people who want to release memory used by the
	            participant factory singleton. Uncomment the following block of
	            code for clean destruction of the participant factory
	            singleton. */
	            //DomainParticipantFactory.finalize_instance();
	        }
	    }
}
