package hu.bme.mit.cps.smartuniversity.calendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import hu.bme.mit.cps.smartuniversity.Entry;
import hu.bme.mit.cps.smartuniversity.EntryDataWriter;
import hu.bme.mit.cps.smartuniversity.EntryTypeSupport;

public class Timetable {
private static int number = 0;
private static int lab_id = 0;
	
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
    	if (args.length >= 2) {
            number = Integer.valueOf(args[0]).intValue();
            lab_id = Integer.valueOf(args[1]).intValue();
        }
    	
    	
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

    private Timetable() {
        super();
    }

    // -----------------------------------------------------------------------

    private static void publisherMain(int domainId, int sampleCount) {

        DomainParticipant participant = null;
        Publisher publisher = null;
        Topic topic = null;
        EntryDataWriter writer = null;

        Map<String, Float> data = new HashMap<String,Float>(); 
        
        System.out.println("Reading resource file");
        
        try {

            File f = new File("./timetable"+number+".txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            while ((readLine = b.readLine()) != null) {
            	String[] separatedData = readLine.split(";");
            	System.out.println(separatedData[0]);
                data.put(separatedData[0], Float.parseFloat(separatedData[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Starting EntryPublisher");
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
            String typeName = EntryTypeSupport.get_type_name();
            EntryTypeSupport.register_type(participant, typeName);

            /* To customize topic QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            topic = participant.create_topic(
                "CurrentTopic",
                typeName, DomainParticipant.TOPIC_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (topic == null) {
                System.err.println("create_topic error\n");
                return;
            }           

            // --- Create writer --- //

            /* To customize data writer QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            writer = (EntryDataWriter)
            publisher.create_datawriter(
                topic, Publisher.DATAWRITER_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (writer == null) {
                System.err.println("create_datawriter error\n");
                return;
            }           

            // --- Write --- //

            /* Create data sample for writing */
            Entry instance = new Entry();
            
            Calendar calendar = Calendar.getInstance();
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            
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
            	
            	if(data.containsKey(formatter.format(calendar.getTime()))) {
                    instance.ECalendarID = number;
                    instance.EValue= data.get(formatter.format(calendar.getTime())); //TODO NULL
                    instance.ELabID = lab_id;
                    instance.TimeStamp = calendar.getInstance().getTimeInMillis();
                    
               
                    System.out.println("Writing Entry" + instance.toString());

                    /* Write data */
                    writer.write(instance, instance_handle);
                    
            	}

                n++;
                
                if(n == data.size()) {
                	n = 0;
                }
                
                
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
