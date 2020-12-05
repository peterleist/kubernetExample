

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from .idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

package hu.bme.mit.cps.k8slab;

import com.rti.dds.infrastructure.*;
import com.rti.dds.infrastructure.Copyable;
import java.io.Serializable;
import com.rti.dds.cdr.CdrHelper;

public class Voltage   implements Copyable, Serializable{

    public long TimeStamp = (long)0;
    public int USensorID = (int)0;
    public float UValue = (float)0;

    public Voltage() {

    }
    public Voltage (Voltage other) {

        this();
        copy_from(other);
    }

    public static Object create() {

        Voltage self;
        self = new  Voltage();
        self.clear();
        return self;

    }

    public void clear() {

        TimeStamp = (long)0;
        USensorID = (int)0;
        UValue = (float)0;
    }

    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }        

        if(getClass() != o.getClass()) {
            return false;
        }

        Voltage otherObj = (Voltage)o;

        if(TimeStamp != otherObj.TimeStamp) {
            return false;
        }
        if(USensorID != otherObj.USensorID) {
            return false;
        }
        if(UValue != otherObj.UValue) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int __result = 0;
        __result += (int)TimeStamp;
        __result += (int)USensorID;
        __result += (int)UValue;
        return __result;
    }

    /**
    * This is the implementation of the <code>Copyable</code> interface.
    * This method will perform a deep copy of <code>src</code>
    * This method could be placed into <code>VoltageTypeSupport</code>
    * rather than here by using the <code>-noCopyable</code> option
    * to rtiddsgen.
    * 
    * @param src The Object which contains the data to be copied.
    * @return Returns <code>this</code>.
    * @exception NullPointerException If <code>src</code> is null.
    * @exception ClassCastException If <code>src</code> is not the 
    * same type as <code>this</code>.
    * @see com.rti.dds.infrastructure.Copyable#copy_from(java.lang.Object)
    */
    public Object copy_from(Object src) {

        Voltage typedSrc = (Voltage) src;
        Voltage typedDst = this;

        typedDst.TimeStamp = typedSrc.TimeStamp;
        typedDst.USensorID = typedSrc.USensorID;
        typedDst.UValue = typedSrc.UValue;

        return this;
    }

    public String toString(){
        return toString("", 0);
    }

    public String toString(String desc, int indent) {
        StringBuffer strBuffer = new StringBuffer();        

        if (desc != null) {
            CdrHelper.printIndent(strBuffer, indent);
            strBuffer.append(desc).append(":\n");
        }

        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("TimeStamp: ").append(TimeStamp).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("USensorID: ").append(USensorID).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("UValue: ").append(UValue).append("\n");  

        return strBuffer.toString();
    }

}
