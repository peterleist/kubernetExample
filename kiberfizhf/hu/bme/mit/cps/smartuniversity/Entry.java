

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from .idl using "rtiddsgen".
The rtiddsgen tool is part of the RTI Connext distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the RTI Connext manual.
*/

package hu.bme.mit.cps.smartuniversity;

import com.rti.dds.infrastructure.*;
import com.rti.dds.infrastructure.Copyable;
import java.io.Serializable;
import com.rti.dds.cdr.CdrHelper;

public class Entry   implements Copyable, Serializable{

    public long TimeStamp = (long)0;
    public int ECalendarID = (int)0;
    public float EValue = (float)0;

    public Entry() {

    }
    public Entry (Entry other) {

        this();
        copy_from(other);
    }

    public static Object create() {

        Entry self;
        self = new  Entry();
        self.clear();
        return self;

    }

    public void clear() {

        TimeStamp = (long)0;
        ECalendarID = (int)0;
        EValue = (float)0;
    }

    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }        

        if(getClass() != o.getClass()) {
            return false;
        }

        Entry otherObj = (Entry)o;

        if(TimeStamp != otherObj.TimeStamp) {
            return false;
        }
        if(ECalendarID != otherObj.ECalendarID) {
            return false;
        }
        if(EValue != otherObj.EValue) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int __result = 0;
        __result += (int)TimeStamp;
        __result += (int)ECalendarID;
        __result += (int)EValue;
        return __result;
    }

    /**
    * This is the implementation of the <code>Copyable</code> interface.
    * This method will perform a deep copy of <code>src</code>
    * This method could be placed into <code>EntryTypeSupport</code>
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

        Entry typedSrc = (Entry) src;
        Entry typedDst = this;

        typedDst.TimeStamp = typedSrc.TimeStamp;
        typedDst.ECalendarID = typedSrc.ECalendarID;
        typedDst.EValue = typedSrc.EValue;

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
        strBuffer.append("ECalendarID: ").append(ECalendarID).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("EValue: ").append(EValue).append("\n");  

        return strBuffer.toString();
    }

}
