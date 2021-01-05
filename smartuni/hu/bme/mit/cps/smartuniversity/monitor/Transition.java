package hu.bme.mit.cps.smartuniversity.monitor;

public class Transition {
    private String id;
    private State sender;
    private State receiver;
    
    public Transition() {
            this.id = "t0";
            this.sender = new State();
            this.receiver = new State();
    }

    public Transition(String id, State sender, State receiver) {
    	if (id.equals("1")) {
    		this.id = "true";
    	} else {
        	this.id = id;
        }
    	
    	if (countChar(this.id, '!') % 2 == 0 && this.id.contains("!")) {
    		formatID();
    	}
    	
        this.sender = sender;
        this.receiver = receiver;
    }
    
    private int countChar(String someString, char someChar) {
		int count = 0;
		 
		for (int i = 0; i < someString.length(); i++) {
		    if (someString.charAt(i) == someChar) {
		        count++;
		    }
		}
		
		return count;
	}
    
    void formatID() {
		this.id = this.id.substring(5, this.id.length() - 1);
	}
    
    public String getMessageType() {
    	String messageType = this.id.substring(2, this.id.length() - 1);
 
    	messageType = messageType.substring(messageType.indexOf(".") + 1);
    	messageType = messageType.substring(0, messageType.indexOf("("));

    	return messageType;
    }
    
    public String[] getParameters() {
    	String messageType = this.id.substring(2, this.id.length() - 1);
    	 
    	messageType = messageType.substring(messageType.indexOf("(") + 1);
    	messageType = messageType.substring(0, messageType.indexOf(")"));

    	if (messageType.equals("")) {
    		return new String[0];
    	}
    	
    	return messageType.split(",");
    }
    
    public String getSenderName() {
    	String sender = this.id;
    	if (this.id.contains("!")) {
    		sender = this.id.substring(2, this.id.length() - 1);
    	}
    	
    	sender = sender.substring(0, sender.indexOf("."));
    	
    	return sender;
    }
    
    public String getReceiverName() {
    	String receiver = this.id;
    	if (this.id.contains("!")) {
    		receiver = this.id.substring(2, this.id.length() - 1);
    	}
    	
    	receiver = receiver.substring(receiver.indexOf(")") + 2);
    	
    	return receiver;
    }


    public String getId() {
        return id;
    }

    public State getSender() {
        return sender;
    }

    public State getReceiver() {
        return receiver;
    }

    public void setReceiver(State receiver) {
        this.receiver = receiver;
    }

    public void setSender(State sender) {
        this.sender = sender;
    }

    public void setId(String id) {
        if(id.equals("1")){
        	this.id = "true";
        }else{
        	this.id = id;
        }
    }
    
    public void writeTransition(){
    	System.out.println(this.id + " " + this.sender.getId() + "->" + this.receiver.getId());
    }
}
