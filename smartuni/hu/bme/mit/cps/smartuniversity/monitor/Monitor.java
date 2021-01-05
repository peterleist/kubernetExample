package hu.bme.mit.cps.smartuniversity.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Monitor implements IMonitor {
	private Automaton automaton;
	private State actualState;
	private List<State> goodStates;
	private boolean requirementFullfilled;
	
	public Monitor(Automaton automaton) {
		this.automaton = automaton;
		this.actualState = automaton.getInitial();
		this.goodStates = new ArrayList<State>();
		this.goodStates.add(automaton.getFinale());
		this.requirementFullfilled = true;
	}
	
	@Override
	public boolean goodStateReached() {
		return this.goodStates.contains(actualState) && this.requirementFullfilled;
	}

	@Override
	public void update(String sender, String receiver, String messageType, String[] parameters) {
		List<Transition> transitions = automaton.findSender(this.actualState);
		String receivedMessage = getReceivedMessage(sender, receiver, messageType, parameters);

		for (Transition transition : transitions) {
			if (!transition.getId().contains("&")) {
				if (!transition.getId().contains("!")
			     && transition.getMessageType().equals(messageType)
			     && transition.getSenderName().equals(sender)
			     && transition.getReceiverName().equals(receiver)
			     && Arrays.equals(transition.getParameters(), parameters)) {
	
					this.actualState = transition.getReceiver();
					updateMonitorStatus(transition);
					return;
				} else if (transition.getId().contains("!")
						&& (!transition.getMessageType().equals(messageType)
						|| !transition.getSenderName().equals(sender)
						|| !transition.getReceiverName().equals(receiver)
						|| !Arrays.equals(transition.getParameters(), parameters))) {
	
					this.actualState = transition.getReceiver();
					updateMonitorStatus(transition);
					return;
				}
			} else if (!transition.getId().contains(receivedMessage)) {
				this.actualState = transition.getReceiver();
				updateMonitorStatus(transition);
				return;
			}
		}
		
		this.requirementFullfilled = false;
		System.out.println("Failure: receivedMessage didn't match any transitions.");
	}
	
	void updateMonitorStatus(Transition transition) {
		System.out.println("transition: " + transition.getId());
		System.out.println(actualState.getId());
				
		/*if (goodStateReached()) {
			Main.monitorStatus("System is in good state.");
		} else {
			Main.monitorStatus("System is in bad state.");
		}*/
	}
	
	String getReceivedMessage(String sender, String receiver, String messageType, String[] parameters) {
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

	@Override
	public boolean requirementSatisfied() {
		return this.actualState == this.automaton.getFinale();
	}

	@Override
	public void errorDetected(String sender, String receiver, String messageType, String[] parameters) {
		// TODO Auto-generated method stub
		
	}
}
