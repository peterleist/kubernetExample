package hu.bme.mit.cps.smartuniversity.monitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Specification{
	private String id = "spec1";
	private ArrayList<Automaton> automatas;
	
	public Specification(){
		automatas = new ArrayList<Automaton>();
		String str;
		String str1;
		String pre;
		String succ;
		State actualState;
		State acceptState;
		State finalState;
		State newState;
		State acceptState_new;
		Automaton a = new Automaton("playlist_generation");
		Automaton b;
		Map<String, Automaton> altauto;
		ArrayList<Automaton> parauto;
		Automaton loopauto;
		Automaton expression;
		int counter = 0;
		
		b = new Automaton("auto7");
		actualState = new State("q" + counter, StateType.NORMAL);
		counter++;
		b.addState(actualState);
		b.setInitial(actualState);
											
		b.addTransition(new Transition("!(" + "validator" + "." +	
			"validateBefore" + "("
			+ ")"
			
			+ "." + "validator)", actualState, actualState));
		
		newState = new State("q" + counter, StateType.FINAL);
		counter++;
		b.addTransition(new Transition("validator" + "." +
		
		"validateBefore" + "("
		+ ")"
		
		+ "." + "validator" , actualState, newState));
		b.addState(newState);
		b.setFinale(newState);
		a.collapse(b);
		
		b = new Automaton("auto7");
		actualState = new State("q" + counter, StateType.NORMAL);
		counter++;
		b.addState(actualState);
		b.setInitial(actualState);
											
		b.addTransition(new Transition("!(" + "validator" + "." +	
			"validateAfter" + "("
			+ ")"
			
			+ "." + "validator)", actualState, actualState));
		
		newState = new State("q" + counter, StateType.FINAL);
		counter++;
		b.addTransition(new Transition("validator" + "." +
		
		"validateAfter" + "("
		+ ")"
		
		+ "." + "validator" , actualState, newState));
		b.addState(newState);
		b.setFinale(newState);
		a.collapse(b);
		
		a.rename();
		automatas.add(a);
	}
	
	public void listAutomatas(){
		for(Automaton a : this.automatas){
			for(State s : a.getStates()){
				s.writeState();	
			}
			
			for(Transition t : a.getTransitions()){
				t.writeTransition();
			}
		}
	}
	
	public List<Automaton> getAutomata() {
		return automatas;
	}
	
	public ArrayList<Automaton> par(ArrayList<Automaton> automatas) {
	        ArrayList<ArrayList<Automaton>> automataList = new ArrayList<>();
	        permute(automataList, new ArrayList<>(), automatas);
	        return listConverter((automataList));
	}

    private void permute(ArrayList<ArrayList<Automaton>> list, ArrayList<Automaton> resultList, ArrayList<Automaton> automatas) {
        if (resultList.size() == automatas.size()) {
            list.add(new ArrayList<>(resultList));
        } else {
            for (int i = 0; i < automatas.size(); i++) {
                if (resultList.contains((automatas.get(i)))) {
                    continue;
                }

                resultList.add(automatas.get(i));
                permute(list, resultList, automatas);
                resultList.remove(resultList.size() - 1);
            }
        }
    }

    private ArrayList<Automaton> listConverter(ArrayList<ArrayList<Automaton>> list) {
        ArrayList<Automaton> result = new ArrayList<>();
        for (ArrayList<Automaton> alist : list) {
            Automaton newauto = new Automaton("listConverter");
            for (Automaton auto : alist) {
                newauto.collapse(copyAutomaton(auto));
            }
            result.add(newauto);
        }
        return result;
    }
    
    public Map<String, Automaton> loopSetup(Automaton loopauto, int min, int max) {
	        	Map<String, Automaton> result = new HashMap<>();
	    	    
	            for (int i = min; i <= max; i++) {
	                Automaton newauto = new Automaton("loopauto" + i);
	                for (int j = 0; j < i; j++) {
	                    newauto.collapse(copyAutomaton(loopauto));
	                }
	                result.put("", newauto);
	            }
	            return result;
	        }
    
    public Automaton copyAutomaton(Automaton referenceAuto) {
            Automaton result = new Automaton("copy automaton");
            int count = 0;
            State previousSender = new State();
            State referencePreviousSender = new State();
    
            for (Transition t : referenceAuto.getTransitions()) {
                State sender = new State();
                State receiver = new State();
                Transition transition = new Transition();
                Automaton temp = new Automaton("temp");
    
                transition.setId(t.getId());
    
                if (t.getSender() == referencePreviousSender) {
                    receiver.setId("c" + count);
                    count++;
                    receiver.setType(t.getReceiver().getType());
    
                    transition.setSender(previousSender);
                    transition.setReceiver(receiver);
                    temp.addState(previousSender);
                    temp.addState(receiver);
                    temp.setInitial(previousSender);
                    temp.setFinale(receiver);
                } else {
                    if (t.getSender() == t.getReceiver()) {
                        sender.setId("c" + count);
                        count++;
                        sender.setType(t.getSender().getType());
    
                        transition.setSender(sender);
                        transition.setReceiver(sender);
    
                        temp.addState(sender);
                        temp.setInitial(sender);
                        temp.setFinale(sender);
                    } else {
                        sender.setId("c" + count);
                        count++;
                        sender.setType(t.getSender().getType());
    
                        receiver.setId("c" + count);
                        count++;
                        receiver.setType(t.getReceiver().getType());
    
                        transition.setSender(sender);
                        transition.setReceiver(receiver);
    
                        temp.addState(sender);
                        temp.addState(receiver);
                        temp.setInitial(sender);
                        temp.setFinale(receiver);
                    }
                    previousSender = sender;
                    referencePreviousSender = t.getSender();
                }
    
                temp.addTransition(transition);
                result.collapse(temp);
            }
    
            return result;
        }
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException{
		Specification specification = new Specification();
		specification.listAutomatas();
		boolean acceptState = false;
		
		PrintWriter writer = new PrintWriter("spec1" + ".txt", "UTF-8");
		for(Automaton a : specification.automatas){
			writer.println("");
			writer.println("never{ /*" + a.getId()+ "Monitor" + "*/");
			for(State s : a.getStates()){
				if(s == a.getInitial()){
					writer.println("T0_init:");
					writer.println(" if");
					for(Transition t : a.findSender(s)){
						if(t.getReceiver() == a.getInitial()){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_init");
						}else if(t.getReceiver().getType().equals(StateType.NORMAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT_ALL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_all" );
						}else if(t.getReceiver().getType().equals(StateType.FINAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_" + t.getReceiver().getId());
						}
					}
					writer.println(" fi;");
				}else if(s.getType().equals(StateType.NORMAL)){
					writer.println("T0_" + s.getId() + ":");
					writer.println(" if");
					for(Transition t : a.findSender(s)){
						if(t.getReceiver() == a.getInitial()){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_init");
						}else if(t.getReceiver().getType().equals(StateType.NORMAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT_ALL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_all" );
						}else if(t.getReceiver().getType().equals(StateType.FINAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_" + t.getReceiver().getId());
						}
					}
					writer.println(" fi;");
				}else if(s.getType().equals(StateType.ACCEPT_ALL) && !acceptState){
					writer.println("accept_all:");
					writer.println("skip");
					acceptState = true;
				}else if(s.getType().equals(StateType.FINAL)){
					writer.println("T0_" + s.getId() + ":");
					writer.println(" if");
					for(Transition t : a.findSender(s)){
						if(t.getReceiver() == a.getInitial()){
							writer.println(" :: (" + t.getId() + ")" + "->" + " goto T0_init");
						}else if(t.getReceiver().getType().equals(StateType.NORMAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT_ALL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_all" );
						}else if(t.getReceiver().getType().equals(StateType.FINAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_" + t.getReceiver().getId());
						}
					}
					writer.println(" fi;");
				}else if(s.getType().equals(StateType.ACCEPT)){
					writer.println("accept_" + s.getId() + ":");
					writer.println(" if");
					for(Transition t : a.findSender(s)){
						if(t.getReceiver() == a.getInitial()){
							writer.println(" :: (" + t.getId() + ")" + "->" + " goto T0_init");
						}else if(t.getReceiver().getType().equals(StateType.NORMAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT_ALL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_all" );
						}else if(t.getReceiver().getType().equals(StateType.FINAL)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto T0_" + t.getReceiver().getId());
						}else if(t.getReceiver().getType().equals(StateType.ACCEPT)){
							writer.println(" :: (" + t.getId() + ") " + "->" + " goto accept_" + t.getReceiver().getId());
						}
					}
					writer.println(" fi;");
				}
				
			}
			writer.println("}");
		}
		writer.close();
		
		PrintWriter xmlWriter = new PrintWriter("spec1" + ".xml", "UTF-8");
		xmlWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xmlWriter.println("<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_1.dtd'>");
		xmlWriter.println("<nta>");
		for (Automaton a : specification.automatas) {
			xmlWriter.println("\t<declaration>");
			String previous = "";
			List<String> existingChannels = new ArrayList<String>();
			for (Transition t : a.getTransitions()) {
				List<String> items = Arrays.asList(t.getId().split("\\s*;\\s*"));
				if (Collections.frequency(existingChannels, items.get(0)) == 0) {
					if (t.getId().startsWith("[") || t.getId().startsWith("![")) {
					} else if (!previous.equals(items.get(0).replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll("\\.", "__").replaceAll("!", "not").replaceAll("&", "_and_").replaceAll("\\s", ""))){
						xmlWriter.println("chan " + items.get(0).replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll("\\.", "__").replaceAll("!", "not").replaceAll("&", "_and_").replaceAll("\\s", "") + ";");
						previous = items.get(0).replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll("\\.", "__").replaceAll("!", "not").replaceAll("&", "_and_").replaceAll("\\s", "");
						existingChannels.add(items.get(0));
					}
				}
			}
			
			
			
			xmlWriter.println("\t</declaration>");
			xmlWriter.println("\t<template>");
			xmlWriter.println("\t\t<name>" + a.getId() + "</name>");
			xmlWriter.println("\t\t<declaration>");
			xmlWriter.println("\t\t</declaration>");
			
			int statecounter = 0;
			
			for (State s : a.getStates()) {
				xmlWriter.println("\t\t<location id=\"" + s.getId() + "\" x=\"" + statecounter + "\" y=\"" + statecounter + "\">");
					if (s.getType().equals(StateType.NORMAL)) {
						xmlWriter.println("\t\t\t<name x=\"" + statecounter + "\" y=\"" + (statecounter + 0.5) + "\">" + s.getId() + "</name>");
					} else if (s.getType().equals(StateType.ACCEPT)) {
						xmlWriter.println("\t\t\t<name x=\"" + statecounter + "\" y=\"" + (statecounter + 0.5) + "\">ACCEPT_" + s.getId() + "</name>");
					} else if (s.getType().equals(StateType.FINAL)) {
						xmlWriter.println("\t\t\t<name x=\"" + statecounter + "\" y=\"" + (statecounter + 0.5) + "\">FINAL_" + s.getId() + "</name>");
					}
				xmlWriter.println("\t\t</location>");
				statecounter++;
			}
			
			xmlWriter.println("\t\t<init ref=\"q0\"/>");
			
			for (Transition t : a.getTransitions()) {
				boolean doubletransition = false;
				xmlWriter.println("\t\t<transition>");
				xmlWriter.println("\t\t\t<source ref=\"" + t.getSender().getId() + "\"/>");
				xmlWriter.println("\t\t\t<target ref=\"" + t.getReceiver().getId() + "\"/>");
				if (t.getId().startsWith("[") || t.getId().startsWith("![")) {
					xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + t.getId().substring(0, t.getId().indexOf("]")).replaceAll("<", "&lt;").replaceAll(">", "&gt;").replace("[", "") + "</label>");
				} else {
					List<String> items = Arrays.asList(t.getId().split("\\s*;\\s*"));

					if (items.size() >= 1) {
						xmlWriter.println("\t\t\t<label kind=\"synchronisation\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(0).replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll("\\.", "__").replaceAll("!", "not").replaceAll("&", "_and_").replaceAll("\\s", "") + "?</label>");
					}

					if (items.size() >= 2) {
						if(items.get(1).contains("1,")) {
							List<String> stringList = Arrays.asList(items.get(1).split("\\s*\\|\\|\\s*"));
							doubletransition = true;
							if (!stringList.get(0).startsWith("(")) {
								xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + stringList.get(0).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", " and") + "</label>");
							} else {
								xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + stringList.get(0).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(",", " and") + "</label>");
							}
						} else {
							if (!items.get(1).startsWith("(")) {
								xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(1).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", " and") + "</label>");
							} else {
								xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(1).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(",", " and") + "</label>");
							}
						}
					}

					if (items.size() >= 3) {
						xmlWriter.println("\t\t\t<label kind=\"assignment\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(2).replaceAll("&", "&amp;") + "</label>");
					}
				}
				xmlWriter.println("\t\t</transition>");
				if (doubletransition) {
					xmlWriter.println("\t\t<transition>");
					xmlWriter.println("\t\t\t<source ref=\"" + t.getSender().getId() + "\"/>");
					xmlWriter.println("\t\t\t<target ref=\"" + t.getReceiver().getId() + "\"/>");
					if (t.getId().startsWith("[") || t.getId().startsWith("![")) {
						xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + t.getId().substring(0, t.getId().indexOf("]")).replaceAll("<", "&lt;").replaceAll(">", "&gt;").replace("[", "") + "</label>");
					} else {
						List<String> items = Arrays.asList(t.getId().split("\\s*;\\s*"));

						if (items.size() >= 1) {
							xmlWriter.println("\t\t\t<label kind=\"synchronisation\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(0).replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll("\\.", "__").replaceAll("!", "not").replaceAll("&", "_and_").replaceAll("\\s", "") + "?</label>");
						}

						if (items.size() >= 2) {
							if(items.get(1).contains("1,")) {
								List<String> stringList = Arrays.asList(items.get(1).split("\\s*\\|\\|\\s*"));
								doubletransition = true;
								if (!stringList.get(1).startsWith("(")) {
									xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + stringList.get(1).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", " and") + "</label>");
								} else {
									xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + stringList.get(1).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(",", " and") + "</label>");
								}
							} else {
								if (!items.get(1).startsWith("(")) {
									xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(1).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", " and") + "</label>");
								} else {
									xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(1).replaceAll("&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(",", " and") + "</label>");
								}
							}
						}

						if (items.size() >= 3) {
							xmlWriter.println("\t\t\t<label kind=\"assignment\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(2).replaceAll("&", "&amp;") + "</label>");
						}
					}
					xmlWriter.println("\t\t</transition>");
				}
			}
			
			xmlWriter.println("\t</template>");
			xmlWriter.println("\t<template>");
			xmlWriter.println("\t\t<name>" + a.getId() + "_environment</name>");
			xmlWriter.println("\t\t<declaration>");
			xmlWriter.println("\t\t</declaration>");
			xmlWriter.println("\t\t<location id=\"q0\" x=\"0\" y=\"0\">");
			xmlWriter.println("\t\t\t<name x=\"1\" y=\"1\">q0</name>");
			xmlWriter.println("\t\t</location>");
			xmlWriter.println("\t\t<init ref=\"q0\"/>");
			
			Set<Transition> unique_transitions = new TreeSet<Transition>(new Comparator<Transition>() {
		        @Override
		        public int compare(Transition t1, Transition t2) {
		        	List<String> items1 = Arrays.asList(t1.getId().split("\\s*;\\s*"));
		        	List<String> items2 = Arrays.asList(t2.getId().split("\\s*;\\s*"));

		            return !(items1.get(0).equals(items2.get(0))) ? 1 : 0;
		        }
		    });
			
			unique_transitions.addAll(a.getTransitions());
			
			for (Transition t : unique_transitions) {
				xmlWriter.println("\t\t<transition>");
				xmlWriter.println("\t\t\t<source ref=\"q0\"/>");
				xmlWriter.println("\t\t\t<target ref=\"q0\"/>");
				if (t.getId().startsWith("[") || t.getId().startsWith("![")) {
					xmlWriter.println("\t\t\t<label kind=\"guard\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + t.getId().substring(0, t.getId().indexOf("]")).replaceAll("<", "&lt;").replaceAll(">", "&gt;").replace("[", "") + "</label>");
				} else {
					List<String> items = Arrays.asList(t.getId().split("\\s*;\\s*"));
	
					if (items.size() >= 1) {
						xmlWriter.println("\t\t\t<label kind=\"synchronisation\" x=\"" + t.getSender().getId().substring(1) + ".5\" y=\"" + t.getSender().getId().substring(1) + ".5\">" + items.get(0).replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll("\\.", "__").replaceAll("!", "not").replaceAll("&", "_and_").replaceAll("\\s", "") + "!</label>");
					}
					
				}
				xmlWriter.println("\t\t</transition>");
			}
			
			xmlWriter.println("\t</template>");
		}
		
		xmlWriter.println("\t<system>");
		xmlWriter.println("receiver = " + specification.automatas.get(0).getId() + "();");
		xmlWriter.println("sender = " + specification.automatas.get(0).getId() + "_environment();");
		xmlWriter.println("system receiver, sender;");
		xmlWriter.println("\t</system>");
		
		xmlWriter.println("</nta>");
		xmlWriter.close();
	}
}
