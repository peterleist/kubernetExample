package hu.bme.mit.cps.smartuniversity.tempvalidator;

import java.util.ArrayList;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.DataReaderListener;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

import hu.bme.mit.cps.smartuniversity.Database;
import hu.bme.mit.cps.smartuniversity.SystemMessage;
import hu.bme.mit.cps.smartuniversity.SystemMessageDataWriter;
import hu.bme.mit.cps.smartuniversity.SystemMessageTypeSupport;
import hu.bme.mit.cps.smartuniversity.Temperature;
import hu.bme.mit.cps.smartuniversity.TemperatureDataReader;
import hu.bme.mit.cps.smartuniversity.TemperatureDataWriter;
import hu.bme.mit.cps.smartuniversity.TemperatureSeq;
import hu.bme.mit.cps.smartuniversity.TemperatureTypeSupport;
import okio.Timeout;

public class TempValidator {
	
	private static int lab_id = 0;
	// -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        // --- Get domain ID --- //
        int domainId = 0;
        if (args.length >= 1) {
        	lab_id = Integer.valueOf(args[0]).intValue();
        }

        /* Uncomment this to turn on additional logging
        Logger.get_instance().set_verbosity_by_category(
            LogCategory.NDDS_CONFIG_LOG_CATEGORY_API,
            LogVerbosity.NDDS_CONFIG_LOG_VERBOSITY_STATUS_ALL);
        */

        // --- Run --- //
        subscriberMain(domainId, 0);
    }

    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------

    private static ArrayList<Temperature> data;
    private static ArrayList<Temperature> dataToleantBefore;
    private static ArrayList<Temperature> dataTolerantAfter;
    private static int time_out = 0;
    
    private static ArrayList<Temperature> initData() {
    	Temperature instance0 = new Temperature();
		instance0.TimeStamp = 0;
		instance0.TSensorID = -1;
		instance0.TLabID = -1;
		instance0.TValue = 0;
		
		Temperature instance1 = new Temperature();
		instance1.TimeStamp = 0;
		instance1.TSensorID = -1;
		instance1.TLabID = -1;
		instance1.TValue = 0;
		
		Temperature instance2 = new Temperature();
		instance2.TimeStamp = 0;
		instance2.TSensorID = -1;
		instance2.TLabID = -1;
		instance2.TValue = 0;
		
		ArrayList<Temperature> list = new ArrayList<Temperature>(3);
		list.add(instance0);
		list.add(instance1);
		list.add(instance2);
    	
    	return list;
	}
    
    private static void printData() {
    	
    	System.out.println("-----------------------------------");
    	
		for(int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i).toString());
		}
	}
        
    private static Temperature validate() {
    	int n = 0;
    	Temperature instance = null;
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i).TSensorID == i) {
				n++;
			}
		}
		
		//printData();
		
		if(n >= 2) {
			System.out.println("Entered big if");
			if (data.get(0).TValue == data.get(1).TValue && data.get(1).TValue == data.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1111;
				instance.TValue = data.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(data.get(0).TimeStamp, Math.max(data.get(1).TimeStamp, data.get(2).TimeStamp));
				data = initData();
				
			}else if (data.get(0).TValue == data.get(1).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1011;
				instance.TValue = data.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(data.get(0).TimeStamp, data.get(1).TimeStamp);
				data = initData();
				
			}else if (data.get(0).TValue == data.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1101;
				instance.TValue = data.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(data.get(0).TimeStamp, data.get(2).TimeStamp);
				data = initData();
				
			}else if (data.get(1).TValue == data.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1110;
				instance.TValue = data.get(1).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(data.get(2).TimeStamp, data.get(1).TimeStamp);
				data = initData();
				
			}
		}
    	
    	return instance;
	}
    
    private static Temperature validateBefore() {
    	int n = 0;
    	Temperature instance = null;
		for (int i = 0; i < dataToleantBefore.size(); i++) {
			if(dataToleantBefore.get(i).TSensorID == i) {
				n++;
			}
		}	
		if(n >= 2) {
			System.out.println("Entered big if");
			if (dataToleantBefore.get(0).TValue == dataToleantBefore.get(1).TValue && dataToleantBefore.get(1).TValue == dataToleantBefore.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1111;
				instance.TValue = dataToleantBefore.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataToleantBefore.get(0).TimeStamp, Math.max(dataToleantBefore.get(1).TimeStamp, dataToleantBefore.get(2).TimeStamp));
				dataToleantBefore = initData();
				
			}else if (dataToleantBefore.get(0).TValue == dataToleantBefore.get(1).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1011;
				instance.TValue = dataToleantBefore.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataToleantBefore.get(0).TimeStamp, dataToleantBefore.get(1).TimeStamp);
				dataToleantBefore = initData();
				
			}else if (dataToleantBefore.get(0).TValue == dataToleantBefore.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1101;
				instance.TValue = dataToleantBefore.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataToleantBefore.get(0).TimeStamp, dataToleantBefore.get(2).TimeStamp);
				dataToleantBefore = initData();
				
			}else if (data.get(1).TValue == dataToleantBefore.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1110;
				instance.TValue = dataToleantBefore.get(1).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataToleantBefore.get(2).TimeStamp, dataToleantBefore.get(1).TimeStamp);
				dataToleantBefore = initData();
				
			}
		}
    	return instance;
	}
    
    private static Temperature validateAfter() {
    	int n = 0;
    	Temperature instance = null;
		for (int i = 0; i < dataTolerantAfter.size(); i++) {
			if(dataTolerantAfter.get(i).TSensorID == i) {
				n++;
			}
		}	
		if(n >= 2) {
			System.out.println("Entered big if");
			if (dataTolerantAfter.get(0).TValue == dataTolerantAfter.get(1).TValue && dataTolerantAfter.get(1).TValue == dataTolerantAfter.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1111;
				instance.TValue = dataTolerantAfter.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataTolerantAfter.get(0).TimeStamp, Math.max(dataTolerantAfter.get(1).TimeStamp, dataTolerantAfter.get(2).TimeStamp));
				dataTolerantAfter = initData();
				
			}else if (dataTolerantAfter.get(0).TValue == dataTolerantAfter.get(1).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1011;
				instance.TValue = dataTolerantAfter.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataTolerantAfter.get(0).TimeStamp, dataTolerantAfter.get(1).TimeStamp);
				dataTolerantAfter = initData();
				
			}else if (dataTolerantAfter.get(0).TValue == dataTolerantAfter.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1101;
				instance.TValue = dataTolerantAfter.get(0).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataTolerantAfter.get(0).TimeStamp, dataTolerantAfter.get(2).TimeStamp);
				dataTolerantAfter = initData();
				
			}else if (data.get(1).TValue == dataTolerantAfter.get(2).TValue) {
				instance = new Temperature();
				instance.TSensorID = 1110;
				instance.TValue = dataTolerantAfter.get(1).TValue;
				instance.TLabID = lab_id;
				instance.TimeStamp = Math.max(dataTolerantAfter.get(2).TimeStamp, dataTolerantAfter.get(1).TimeStamp);
				dataTolerantAfter = initData();
				
			}
		}
    	return instance;
	}
    
    // --- Constructors: -----------------------------------------------------

    private TempValidator() {
        super();
    }

    // -----------------------------------------------------------------------

    private static void subscriberMain(int domainId, int sampleCount) {

        DomainParticipant participant = null;
        Subscriber subscriber = null;
        Publisher publisher = null;
        Topic topic = null;
        Topic monitorTopic = null;
        DataReaderListener listener = null;
        TemperatureDataReader reader = null;
        TemperatureDataWriter writer = null;
        SystemMessageDataWriter monitorWriter = null;
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
            String typeName = TemperatureTypeSupport.get_type_name(); 
            TemperatureTypeSupport.register_type(participant, typeName);

            /* To customize topic QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            topic = participant.create_topic(
                "TempTopic",
                typeName, DomainParticipant.TOPIC_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (topic == null) {
                System.err.println("create_topic error\n");
                return;
            }
            
            String monitorTypeName = SystemMessageTypeSupport.get_type_name();
            SystemMessageTypeSupport.register_type(participant, monitorTypeName);

            /* To customize topic QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            monitorTopic = participant.create_topic(
                "MonitorTopic",
                monitorTypeName, DomainParticipant.TOPIC_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (monitorTopic == null) {
                System.err.println("create_topic error\n");
                return;
            }
            
            // --- Create writer --- //

            /* To customize data writer QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            writer = (TemperatureDataWriter)
            publisher.create_datawriter(
                topic, Publisher.DATAWRITER_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (writer == null) {
                System.err.println("create_datawriter error\n");
                return;
            }
            
            monitorWriter = (SystemMessageDataWriter)
            publisher.create_datawriter(
                monitorTopic, Publisher.DATAWRITER_QOS_DEFAULT,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (monitorWriter == null) {
                System.err.println("create_datawriter error\n");
                return;
            }

            // --- Create reader --- //

            listener = new TempListener();

            /* To customize data reader QoS, use
            the configuration file USER_QOS_PROFILES.xml */

            reader = (TemperatureDataReader)
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
                System.out.println("Temperature subscriber sleeping for "
                + receivePeriodSec + " sec...");

                Temperature valid = validate();
                
                if(valid != null) {
                	System.out.println("Valid Temperature" + valid.toString());
                	writer.write(valid, instance_handle);
                	db.addData(valid.TimeStamp, valid.TValue, valid.TLabID, "lab");
                	time_out = 0;
                	
                	if (count != 0) {
	                	SystemMessage message1 = new SystemMessage();
	            		message1.TimeStamp = 2;
	            		message1.SMessage = "validator validator validateAfter";
	            		monitorWriter.write(message1, instance_handle);
	            		System.out.println("TempValidator sent after message to monitor.");
                	} else {
                		SystemMessage message = new SystemMessage();
                		message.TimeStamp = 1;
                		message.SMessage = "validator validator validateBefore";
                		monitorWriter.write(message, instance_handle);
                		System.out.println("TempValidator sent before message to monitor.");
                	}
                	
                }
                
                if(time_out >= 10) {
                	Temperature validAfter = validateAfter();
                	
                	if(validAfter != null) {
                		writer.write(validAfter, instance_handle);
                		db.addData(validAfter.TimeStamp, validAfter.TValue, validAfter.TLabID, "lab");
                	}
                	else {
                		Temperature validBefore = validateBefore();
                		
                		if(validBefore != null) {
                    		writer.write(validBefore, instance_handle);
                    		db.addData(validBefore.TimeStamp, validBefore.TValue, validBefore.TLabID, "lab");
                    	}
                	}
                }
                else {
                	time_out++;
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

    private static class TempListener extends DataReaderAdapter {

    	TemperatureSeq _dataSeq = new TemperatureSeq();
        SampleInfoSeq _infoSeq = new SampleInfoSeq();

        public void on_data_available(DataReader reader) {
        	TemperatureDataReader voltageReader = (TemperatureDataReader)reader;

            try {
                voltageReader.take(
                    _dataSeq, _infoSeq,
                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                    SampleStateKind.ANY_SAMPLE_STATE,
                    ViewStateKind.ANY_VIEW_STATE,
                    InstanceStateKind.ANY_INSTANCE_STATE);
                
                System.out.println("Size: " + _dataSeq.size());

                for(int i = 0; i < _dataSeq.size(); ++i) {
                    SampleInfo info = (SampleInfo)_infoSeq.get(i);

                    if (info.valid_data) {
                    	
                    	//System.out.println(_dataSeq.get(i));
                    	Temperature instance = new Temperature(_dataSeq.get(i));
                    	
                        //System.out.println((instance.toString("Received",0)));
                        
                        if(instance.TSensorID < 999) {
                        	System.out.println(instance.toString());
                        	//System.out.println(instance.USensorID);
                        	if( instance.TLabID == lab_id) {
                        	data.set(instance.TSensorID, instance);
                            //printData();
                        	}
                        	
                        	if(instance.TLabID == lab_id-1) {
                        	dataToleantBefore.set(instance.TSensorID, instance);	
                        	}
                        	
                        	if(instance.TLabID == lab_id+1) {
                        	dataTolerantAfter.set(instance.TSensorID, instance);		
                        	}
                        }
                        
                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No data to process
            } finally {
                voltageReader.return_loan(_dataSeq, _infoSeq);
            }
        }
    }
}
