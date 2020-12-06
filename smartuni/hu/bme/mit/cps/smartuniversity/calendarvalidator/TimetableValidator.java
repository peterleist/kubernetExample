

package hu.bme.mit.cps.smartuniversity.calendarvalidator;

/* PowerSubscriber.java

A publication of data of type Power

This file is derived from code automatically generated by the rtiddsgen 
command:

rtiddsgen -language java -example <arch> .idl

Example publication of type Power automatically generated by 
'rtiddsgen' To test them follow these steps:

(1) Compile this file and the example subscription.

(2) Start the subscription on the same domain used for RTI Data Distribution
Service with the command
java PowerSubscriber <domain_id> <sample_count>

(3) Start the publication on the same domain used for RTI Data Distribution
Service with the command
java PowerPublisher <domain_id> <sample_count>

(4) [Optional] Specify the list of discovery initial peers and 
multicast receive addresses via an environment variable or a file 
(in the current working directory) called NDDS_DISCOVERY_PEERS. 

You can run any number of publishers and subscribers programs, and can 
add and remove them dynamically from the domain.

Example:

To run the example application on domain <domain_id>:

Ensure that $(NDDSHOME)/lib/<arch> is on the dynamic library path for
Java.                       

On UNIX systems: 
add $(NDDSHOME)/lib/<arch> to the 'LD_LIBRARY_PATH' environment
variable

On Windows systems:
add %NDDSHOME%\lib\<arch> to the 'Path' environment variable

Run the Java applications:

java -Djava.ext.dirs=$NDDSHOME/class PowerPublisher <domain_id>

java -Djava.ext.dirs=$NDDSHOME/class PowerSubscriber <domain_id>  
*/

import java.util.ArrayList;
import java.util.Calendar;

import com.rti.dds.domain.*;
import com.rti.dds.infrastructure.*;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.*;
import com.rti.dds.topic.*;

import hu.bme.mit.cps.smartuniversity.Database;
import hu.bme.mit.cps.smartuniversity.Entry;
import hu.bme.mit.cps.smartuniversity.EntryDataReader;
import hu.bme.mit.cps.smartuniversity.EntryDataWriter;
import hu.bme.mit.cps.smartuniversity.EntrySeq;
import hu.bme.mit.cps.smartuniversity.EntryTypeSupport;

// ===========================================================================

public class TimetableValidator {
	private static int lab_id = 0;
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
    	if (args.length >= 1) {
            lab_id = Integer.valueOf(args[0]).intValue();
        }
    
        // --- Get domain ID --- //
        int domainId = 0;
        //if (args.length >= 1) {
        //    domainId = Integer.valueOf(args[0]).intValue();
        //}

        // -- Get max loop count; 0 means infinite loop --- //
        int sampleCount = 0;
        //if (args.length >= 2) {
        //    sampleCount = Integer.valueOf(args[1]).intValue();
        //}

        /* Uncomment this to turn on additional logging
        Logger.get_instance().set_verbosity_by_category(
            LogCategory.NDDS_CONFIG_LOG_CATEGORY_API,
            LogVerbosity.NDDS_CONFIG_LOG_VERBOSITY_STATUS_ALL);
        */

        // --- Run --- //
        subscriberMain(domainId, sampleCount);
    }

    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------

    private static ArrayList<Entry> data;
    
    private static ArrayList<Entry> initData() {
    	Entry instance0 = new Entry();
		instance0.TimeStamp = 0;
		instance0.ECalendarID = -1;
		instance0.EValue = 0;
		
		Entry instance1 = new Entry();
		instance1.TimeStamp = 0;
		instance1.ECalendarID = -1;
		instance1.EValue = 0;
		
		ArrayList<Entry> list = new ArrayList<Entry>(2);
		list.add(instance0);
		list.add(instance1);
    	
    	return list;
	}
    
    private static void printData() {
    	
    	System.out.println("-----------------------------------");
    	
		for(int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i).toString());
		}
	}
        
    private static Entry validate() {
    	System.out.println("Validate: " + data.size());
    	int n = 0;
    	Entry instance = null;
    	Calendar calendar = Calendar.getInstance();
    	
		for (int i = 0; i < data.size(); i++) {
			System.out.println("CalendarID: " + data.get(i).ECalendarID + " i: " + i);
			if(data.get(i).ECalendarID == i) {
				n++;
			}
		}
		System.out.println("N: " + n);
		
		if(n >= 1) {
			System.out.println("Entered big if");
			System.out.println("Data0: " + data.get(0) + " Data1: " + data.get(1));
			if ((data.get(0).EValue == 1 || data.get(0).EValue == 0) && data.get(0).ELabID == lab_id) {
				instance = new Entry();
				instance.ECalendarID = data.get(0).ECalendarID;
				instance.EValue = data.get(0).EValue;
				instance.TimeStamp = calendar.getInstance().getTimeInMillis();
				data = initData();
			} else if ((data.get(1).EValue == 0 || data.get(1).EValue == 1) && data.get(1).ELabID == lab_id) {
				instance = new Entry();
				instance.ECalendarID = data.get(1).ECalendarID;
				instance.EValue = data.get(1).EValue;
				instance.TimeStamp = calendar.getInstance().getTimeInMillis();
				data = initData(); 
			} else if (data.get(0).EValue == 1 || data.get(0).EValue == 0) {
				instance = new Entry();
				instance.ECalendarID = data.get(0).ECalendarID;
				instance.EValue = data.get(0).EValue;
				instance.TimeStamp = calendar.getInstance().getTimeInMillis();
				data = initData();
			} else if (data.get(1).EValue == 0 || data.get(1).EValue == 1) {
				instance = new Entry();
				instance.ECalendarID = data.get(1).ECalendarID;
				instance.EValue = data.get(1).EValue;
				instance.TimeStamp = calendar.getInstance().getTimeInMillis();
				data = initData();
			}
		}
    	
    	return instance;
	}
    
    // --- Constructors: -----------------------------------------------------

    private TimetableValidator() {
        super();
    }

    // -----------------------------------------------------------------------

    private static void subscriberMain(int domainId, int sampleCount) {

        DomainParticipant participant = null;
        Subscriber subscriber = null;
        Publisher publisher = null;
        Topic topic = null;
        DataReaderListener listener = null;
        EntryDataReader reader = null;
        EntryDataWriter writer = null;
        Database db = new Database();
        data = initData();
        
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

            // --- Create subscriber --- //

            /* To customize subscriber QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            subscriber = participant.create_subscriber(
                DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null /* listener */,
                StatusKind.STATUS_MASK_NONE);
            if (subscriber == null) {
                System.err.println("create_subscriber error\n");
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
                "EntryTopic",
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

            // --- Create reader --- //

            listener = new CurrentListener();

            /* To customize data reader QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            reader = (EntryDataReader)
            subscriber.create_datareader(
                topic, Subscriber.DATAREADER_QOS_DEFAULT, listener,
                StatusKind.STATUS_MASK_ALL);
            if (reader == null) {
                System.err.println("create_datareader error\n");
                return;
            }                         

            // --- Wait for data --- //
            InstanceHandle_t instance_handle = InstanceHandle_t.HANDLE_NIL;
            final long receivePeriodSec = 10;

            for (int count = 0;
            (sampleCount == 0) || (count < sampleCount);
            ++count) {
                System.out.println("Timetable subscriber sleeping for "
                + receivePeriodSec + " sec...");

                Entry valid = validate();
                
                if(valid != null) {
                	System.out.println("Valid Entry" + valid.toString());
                	writer.write(valid, instance_handle);
                	db.addData(valid.TimeStamp, valid.EValue, valid.ELabID, "calendar");
                }
                
                try {
                    Thread.sleep(receivePeriodSec * 1000);  // in millisec
                } catch (InterruptedException ix) {
                    System.err.println("INTERRUPTED");
                    break;
                }
            }
        } finally {

            // --- Shutdown --- //

            if(participant != null) {
                participant.delete_contained_entities();

                DomainParticipantFactory.TheParticipantFactory.
                delete_participant(participant);
            }
            /* RTI Data Distribution Service provides the finalize_instance()
            method for users who want to release memory used by the
            participant factory singleton. Uncomment the following block of
            code for clean destruction of the participant factory
            singleton. */
            //DomainParticipantFactory.finalize_instance();
        }
    }

    // -----------------------------------------------------------------------
    // Private Types
    // -----------------------------------------------------------------------

    // =======================================================================

    private static class CurrentListener extends DataReaderAdapter {

    	EntrySeq _dataSeq = new EntrySeq();
        SampleInfoSeq _infoSeq = new SampleInfoSeq();

        public void on_data_available(DataReader reader) {
        	EntryDataReader currentReader =
            (EntryDataReader)reader;

            try {
                currentReader.take(
                    _dataSeq, _infoSeq,
                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                    SampleStateKind.ANY_SAMPLE_STATE,
                    ViewStateKind.ANY_VIEW_STATE,
                    InstanceStateKind.ANY_INSTANCE_STATE);

                for(int i = 0; i < _dataSeq.size(); ++i) {
                    SampleInfo info = (SampleInfo)_infoSeq.get(i);

                    if (info.valid_data) {
                    	Entry instance = new Entry(_dataSeq.get(i));
                        
                        if(instance.ECalendarID < 999) {
                        	data.set(instance.ECalendarID, instance);
                        }
                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No data to process
            } finally {
                currentReader.return_loan(_dataSeq, _infoSeq);
            }
        }
    }
}

