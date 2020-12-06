

package hu.bme.mit.cps.smartuniversity.adjustmentcalculator;

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

import com.rti.dds.domain.*;
import com.rti.dds.infrastructure.*;
import com.rti.dds.subscription.*;
import com.rti.dds.topic.*;

import hu.bme.mit.cps.smartuniversity.Entry;
import hu.bme.mit.cps.smartuniversity.EntryDataReader;
import hu.bme.mit.cps.smartuniversity.EntrySeq;
import hu.bme.mit.cps.smartuniversity.EntryTypeSupport;
import hu.bme.mit.cps.smartuniversity.EnvironmentTemperature;
import hu.bme.mit.cps.smartuniversity.EnvironmentTemperatureDataReader;
import hu.bme.mit.cps.smartuniversity.EnvironmentTemperatureSeq;
import hu.bme.mit.cps.smartuniversity.EnvironmentTemperatureTypeSupport;
import hu.bme.mit.cps.smartuniversity.Database;
import hu.bme.mit.cps.smartuniversity.Adjustment;
import hu.bme.mit.cps.smartuniversity.Temperature;
import hu.bme.mit.cps.smartuniversity.TemperatureDataReader;
import hu.bme.mit.cps.smartuniversity.TemperatureSeq;
import hu.bme.mit.cps.smartuniversity.TemperatureTypeSupport;

// ===========================================================================

public class AdjustmentCalculator {
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

    private static Temperature temp = null;
    private static Entry entry = null;
    private static EnvironmentTemperature envTemp = null;
    
    
    private static void initData() {
    	temp = null;
    	entry = null;
    	envTemp = null;
    }
    
    private static void printData() {
		System.out.print("Temperature " + temp.toString() + "\nEntry " + entry.toString() + "\nEnvTemp " + envTemp.toString());
	}
    
    private static Adjustment validate() {
    	Adjustment adjustment = null;
    	if (temp != null && entry != null && entry.ELabID == lab_id) {
    		printData();
    		
			if ((((temp.TimeStamp/1000)-(entry.TimeStamp/1000)) <= 30) && entry.EValue == 1) {
				adjustment = new Adjustment();
				calculateAdjustmentWork(adjustment);
			} else if ((((temp.TimeStamp/1000)-(entry.TimeStamp/1000)) <= 30) && entry.EValue == 0) {
				adjustment = new Adjustment();
				calculateAdjustmentNoWork(adjustment);
			}
		} else if (temp != null) {
			if (((temp.TimeStamp/1000)-(entry.TimeStamp/1000)) <= 30) {
				adjustment = new Adjustment();
				calculateAdjustmentWork(adjustment);
			}
		}
    	
    	return adjustment;
    }
    
    // --- Constructors: -----------------------------------------------------

    private static void calculateAdjustmentWork(Adjustment adjustment) {
    	if (temp.TValue < 21) {
			adjustment.AValue = Math.min(21 - temp.TValue, 10);
			adjustment.TimeStamp = Math.max(temp.TimeStamp, entry.TimeStamp);
			adjustment.ALabID = lab_id;
		} else if (temp.TValue > 24) {
			adjustment.AValue = Math.max(24 - temp.TValue, -10);
			adjustment.TimeStamp = Math.max(temp.TimeStamp, entry.TimeStamp);
			adjustment.ALabID = lab_id;
		}
    }
    
    private static void calculateAdjustmentNoWork(Adjustment adjustment) {
    	float avg = (envTemp.TValue + temp.TValue) / 2;

    	if (temp.TValue < 14) {
			adjustment.AValue = Math.min(14 - avg, 10);
			adjustment.TimeStamp = Math.max(temp.TimeStamp, entry.TimeStamp);
			adjustment.ALabID = lab_id;
		} else if (temp.TValue > 26) {
			adjustment.AValue = Math.max(26 - avg, -10);
			adjustment.TimeStamp = Math.max(temp.TimeStamp, entry.TimeStamp);
			adjustment.ALabID = lab_id;
		}
    }
    
    private AdjustmentCalculator() {
        super();
    }

    // -----------------------------------------------------------------------

    private static void subscriberMain(int domainId, int sampleCount) {

        DomainParticipant participant = null;
        Subscriber subscriber = null;
        Topic entryTopic = null;
        Topic temperatureTopic = null;
        Topic weatherTopic = null;
        TemperatureListener temperatureListener = null;
        EntryListener entryListener = null;
        WeatherListener weatherListener = null;
        EntryDataReader entryReader = null;
        TemperatureDataReader temperatureReader = null;
        EnvironmentTemperatureDataReader weatherReader = null;
        Database db = new Database();
        
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

            // --- Create topic --- //

            /* Register type before creating topic */
            String temperatureTypeName = TemperatureTypeSupport.get_type_name(); 
            TemperatureTypeSupport.register_type(participant, temperatureTypeName);
            String entryTypeName = EntryTypeSupport.get_type_name(); 
            EntryTypeSupport.register_type(participant, entryTypeName);
            String weatherTypeName = EnvironmentTemperatureTypeSupport.get_type_name();
            EnvironmentTemperatureTypeSupport.register_type(participant, weatherTypeName);

            /* To customize topic QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            temperatureTopic = participant.create_topic(
                "TempTopic",
                temperatureTypeName, DomainParticipant.TOPIC_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (temperatureTopic == null) {
                System.err.println("create_topic error\n");
                return;
            }
            
            entryTopic = participant.create_topic(
                "EntryTopic",
                entryTypeName, DomainParticipant.TOPIC_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (entryTopic == null) {
                System.err.println("create_topic error\n");
                return;
            }
            
            weatherTopic = participant.create_topic(
        		"WeatherTopic",
        		weatherTypeName, DomainParticipant.TOPIC_QOS_DEFAULT,
        		null /* listener */, StatusKind.STATUS_MASK_NONE);
        	if (weatherTopic == null) {
        		System.err.println("create_topic error\n");
        		return;
        	}

            // --- Create reader --- //

            temperatureListener = new TemperatureListener();

            /* To customize data reader QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            temperatureReader = (TemperatureDataReader)
            subscriber.create_datareader(
            	temperatureTopic, Subscriber.DATAREADER_QOS_DEFAULT, temperatureListener,
                StatusKind.STATUS_MASK_ALL);
            if (temperatureReader == null) {
                System.err.println("create_datareader error\n");
                return;
            }   
            
            entryListener = new EntryListener();

            /* To customize data reader QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            entryReader = (EntryDataReader)
            subscriber.create_datareader(
            	entryTopic, Subscriber.DATAREADER_QOS_DEFAULT, entryListener,
                StatusKind.STATUS_MASK_ALL);
            if (entryReader == null) {
                System.err.println("create_datarea)der error\n");
                return;
            }
            
            weatherListener = new WeatherListener();
            
            
            weatherReader = (EnvironmentTemperatureDataReader)
            subscriber.create_datareader(
        		weatherTopic, Subscriber.DATAREADER_QOS_DEFAULT, weatherListener,
        		StatusKind.STATUS_MASK_ALL);
            if (weatherReader == null) {
            	System.err.println("create_datareader error\n");
            }
            
            

            // --- Wait for data --- //

            final long receivePeriodSec = 4;
            Adjustment instance = null;
            
            for (int count = 0;
            (sampleCount == 0) || (count < sampleCount);
            ++count) {
                System.out.println("Adjustment subscriber sleeping for "
                + receivePeriodSec + " sec...");

            	instance = validate(); 
            	if (instance != null) {
					System.out.println("Valid Adjustment" + instance.toString());
					db.addData(instance.TimeStamp, instance.AValue);
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

    private static class TemperatureListener extends DataReaderAdapter {

        TemperatureSeq _dataSeq = new TemperatureSeq();
        SampleInfoSeq _infoSeq = new SampleInfoSeq();

        public void on_data_available(DataReader reader) {
        	TemperatureDataReader temperatureReader =
            (TemperatureDataReader)reader;

            try {
            	temperatureReader.take(
                    _dataSeq, _infoSeq,
                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                    SampleStateKind.ANY_SAMPLE_STATE,
                    ViewStateKind.ANY_VIEW_STATE,
                    InstanceStateKind.ANY_INSTANCE_STATE);

                for(int i = 0; i < _dataSeq.size(); ++i) {
                    SampleInfo info = (SampleInfo)_infoSeq.get(i);

                    if (info.valid_data) {
                    	Temperature instance = _dataSeq.get(i);
                        if (instance.TSensorID > 999) {
							temp = instance;
						}
                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No data to process
            } finally {
            	temperatureReader.return_loan(_dataSeq, _infoSeq);
            }
        }
    }
    
    private static class EntryListener extends DataReaderAdapter {

        EntrySeq _dataSeq = new EntrySeq();
        SampleInfoSeq _infoSeq = new SampleInfoSeq();

        public void on_data_available(DataReader reader) {
        	EntryDataReader entryReader =
            (EntryDataReader)reader;

            try {
            	entryReader.take(
                    _dataSeq, _infoSeq,
                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                    SampleStateKind.ANY_SAMPLE_STATE,
                    ViewStateKind.ANY_VIEW_STATE,
                    InstanceStateKind.ANY_INSTANCE_STATE);

                for(int i = 0; i < _dataSeq.size(); ++i) {
                    SampleInfo info = (SampleInfo)_infoSeq.get(i);

                    if (info.valid_data) {
                    	Entry instance = _dataSeq.get(i);
                    	if (instance.ECalendarID < 999) {
							entry = instance;
						}
                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No data to process
            } finally {
            	entryReader.return_loan(_dataSeq, _infoSeq);
            }
        }
    }
    
    private static class WeatherListener extends DataReaderAdapter {
    	EnvironmentTemperatureSeq _dataSeq = new EnvironmentTemperatureSeq();
    	SampleInfoSeq _infoSeq = new SampleInfoSeq();
    	
    	public void on_data_available(DataReader reader) {
    		EnvironmentTemperatureDataReader weatherReader =
    		(EnvironmentTemperatureDataReader)reader;
    		
    		try {
    			weatherReader.take(
                    _dataSeq, _infoSeq,
                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                    SampleStateKind.ANY_SAMPLE_STATE,
                    ViewStateKind.ANY_VIEW_STATE,
                    InstanceStateKind.ANY_INSTANCE_STATE);

                for(int i = 0; i < _dataSeq.size(); ++i) {
                    SampleInfo info = (SampleInfo)_infoSeq.get(i);

                    if (info.valid_data) {
                    	EnvironmentTemperature instance = _dataSeq.get(i);
                    	if (instance.TValue < 999) {
							envTemp = instance;
						}
                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No data to process
            } finally {
            	weatherReader.return_loan(_dataSeq, _infoSeq);
            }
        }
    }
}
