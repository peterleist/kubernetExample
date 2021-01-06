package hu.bme.mit.cps.smartuniversity.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

import hu.bme.mit.cps.smartuniversity.Database;
import hu.bme.mit.cps.smartuniversity.SystemMessage;
import hu.bme.mit.cps.smartuniversity.SystemMessageDataReader;
import hu.bme.mit.cps.smartuniversity.SystemMessageSeq;
import hu.bme.mit.cps.smartuniversity.SystemMessageTypeSupport;
import hu.bme.mit.cps.smartuniversity.SystemState;

import hu.bme.mit.cps.smartuniversity.TemperatureTypeSupport;

public class Monitor {
	private static Automaton automaton;
	private static State actualState;
	private static List<State> goodStates;
	private static boolean requirementFullfilled;
	
	private static SystemMessage systemMessage = null;
	
	public static void main(String[] args) {
		//if (args.length >= 1) {
		//	lab_id = Integer.valueOf(args[0]).intValue();
		//}

		// --- Get domain ID --- //
		int domainId = 0;
		// if (args.length >= 1) {
		// domainId = Integer.valueOf(args[0]).intValue();
		// }

		// -- Get max loop count; 0 means infinite loop --- //
		int sampleCount = 0;
		// if (args.length >= 2) {
		// sampleCount = Integer.valueOf(args[1]).intValue();
		// }

		/*
		 * Uncomment this to turn on additional logging
		 * Logger.get_instance().set_verbosity_by_category(
		 * LogCategory.NDDS_CONFIG_LOG_CATEGORY_API,
		 * LogVerbosity.NDDS_CONFIG_LOG_VERBOSITY_STATUS_ALL);
		 */

		// --- Run --- //
		subscriberMain(domainId, sampleCount);
	}
	
	public Monitor(Automaton automaton) {
		Monitor.automaton = automaton;
		this.actualState = automaton.getInitial();
		this.goodStates = new ArrayList<State>();
		this.goodStates.add(automaton.getFinale());
		this.requirementFullfilled = true;
	}
	
	public static boolean goodStateReached() {
		return !actualState.getType().equals(StateType.ACCEPT);
	}

	public static void update(String sender, String receiver, String messageType, String[] parameters) {
		List<Transition> transitions = automaton.findSender(actualState);
		String receivedMessage = getReceivedMessage(sender, receiver, messageType, parameters);

		for (Transition transition : transitions) {
			if (!transition.getId().contains("&")) {
				if (!transition.getId().contains("!")
			     && transition.getMessageType().equals(messageType)
			     && transition.getSenderName().equals(sender)
			     && transition.getReceiverName().equals(receiver)
			     && Arrays.equals(transition.getParameters(), parameters)) {
	
					actualState = transition.getReceiver();
					updateMonitorStatus(transition);
					return;
				} else if (transition.getId().contains("!")
						&& (!transition.getMessageType().equals(messageType)
						|| !transition.getSenderName().equals(sender)
						|| !transition.getReceiverName().equals(receiver)
						|| !Arrays.equals(transition.getParameters(), parameters))) {
	
					actualState = transition.getReceiver();
					updateMonitorStatus(transition);
					return;
				}
			} else if (!transition.getId().contains(receivedMessage)) {
				actualState = transition.getReceiver();
				updateMonitorStatus(transition);
				return;
			}
		}
		
		requirementFullfilled = false;
		System.out.println("Failure: receivedMessage didn't match any transitions.");
	}
	
	static void updateMonitorStatus(Transition transition) {
		System.out.println("transition: " + transition.getId());
		System.out.println(actualState.getId());
				
		/*if (goodStateReached()) {
			Main.monitorStatus("System is in good state.");
		} else {
			Main.monitorStatus("System is in bad state.");
		}*/
	}
	
	static String getReceivedMessage(String sender, String receiver, String messageType, String[] parameters) {
		String receivedMessage = sender + "." + messageType + "(";
		for (String param : parameters) {
			receivedMessage += param;
			if (!(parameters[parameters.length - 1]).equals(param)) {
				receivedMessage += ", ";
			}
		}
		receivedMessage += ")." + receiver;
		
		return receivedMessage;
	}

	public static boolean requirementSatisfied() {
		return actualState.getType().equals(StateType.FINAL);
	}
	
	private static void subscriberMain(int domainId, int sampleCount) {

		DomainParticipant participant = null;
		Subscriber subscriber = null;
		Topic monitorTopic = null;
		SystemListener systemListener = null;
		SystemMessageDataReader systemMessageReader = null;
		Database db = new Database();

		try {

			// --- Create participant --- //

			/*
			 * To customize participant QoS, use the configuration file
			 * USER_QOS_PROFILES.xml
			 */

			participant = DomainParticipantFactory.TheParticipantFactory.create_participant(domainId,
					DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null /* listener */, StatusKind.STATUS_MASK_NONE);
			if (participant == null) {
				System.err.println("create_participant error\n");
				return;
			}

			// --- Create subscriber --- //

			/*
			 * To customize subscriber QoS, use the configuration file USER_QOS_PROFILES.xml
			 */

			subscriber = participant.create_subscriber(DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null /* listener */,
					StatusKind.STATUS_MASK_NONE);
			if (subscriber == null) {
				System.err.println("create_subscriber error\n");
				return;
			}

			// --- Create topic --- //

			/* Register type before creating topic */
			String systemMessageTypeName = SystemMessageTypeSupport.get_type_name();
			TemperatureTypeSupport.register_type(participant, systemMessageTypeName);

			/*
			 * To customize topic QoS, use the configuration file USER_QOS_PROFILES.xml
			 */

			monitorTopic = participant.create_topic("MonitorTopic", systemMessageTypeName,
					DomainParticipant.TOPIC_QOS_DEFAULT, null /* listener */, StatusKind.STATUS_MASK_NONE);
			if (monitorTopic == null) {
				System.err.println("create_topic error\n");
				return;
			}

			// --- Create reader --- //

			systemListener = new SystemListener();

			/*
			 * To customize data reader QoS, use the configuration file
			 * USER_QOS_PROFILES.xml
			 */

			systemMessageReader = (SystemMessageDataReader) subscriber.create_datareader(monitorTopic,
					Subscriber.DATAREADER_QOS_DEFAULT, systemListener, StatusKind.STATUS_MASK_ALL);
			if (systemMessageReader == null) {
				System.err.println("create_datareader error\n");
				return;
			}

			// --- Wait for data --- //

			final long receivePeriodSec = 4;
			SystemState instance = null;

			for (int count = 0; (sampleCount == 0) || (count < sampleCount); ++count) {
				System.out.println("System Message subscriber sleeping for " + receivePeriodSec + " sec...");

				String[] splitSystemMessage = systemMessage.SMessage.split(".");
				update(splitSystemMessage[0], splitSystemMessage[1], splitSystemMessage[2], new String[] {});
				instance.TimeStamp = Calendar.getInstance().getTimeInMillis();
				instance.SState = requirementSatisfied() ? 1 : 0;
				if (instance != null) {
					System.out.println("Valid SystemState" + instance.toString());
					db.addData(instance.TimeStamp, instance.SState, "state");
				}

				try {
					Thread.sleep(receivePeriodSec * 1000); // in millisec
				} catch (InterruptedException ix) {
					System.err.println("INTERRUPTED");
					break;
				}
			}
		} finally {

			// --- Shutdown --- //

			if (participant != null) {
				participant.delete_contained_entities();

				DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
			}
			/*
			 * RTI Data Distribution Service provides the finalize_instance() method for
			 * users who want to release memory used by the participant factory singleton.
			 * Uncomment the following block of code for clean destruction of the
			 * participant factory singleton.
			 */
			// DomainParticipantFactory.finalize_instance();
		}
	}
	
	private static class SystemListener extends DataReaderAdapter {

    	SystemMessageSeq _dataSeq = new SystemMessageSeq();
        SampleInfoSeq _infoSeq = new SampleInfoSeq();

        public void on_data_available(DataReader reader) {
        	SystemMessageDataReader voltageReader = (SystemMessageDataReader)reader;

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
                    	SystemMessage instance = new SystemMessage(_dataSeq.get(i));
                    	
                        //System.out.println((instance.toString("Received",0)));
                        
                        if (instance != null) {
                        	systemMessage = instance;
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
