

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

public class Temperature   implements Copyable, Serializable{

    public long TimeStamp = (long)0;
    public int TSensorID = (int)0;
    public float TValue = (float)0;

    public Temperature() {

    }
    public Temperature (Temperature other) {

        this();
        copy_from(other);
    }

    public static Object create() {

        Temperature self;
        self = new  Temperature();
        self.clear();
        return self;

    }

    public void clear() {

        TimeStamp = (long)0;
        TSensorID = (int)0;
        TValue = (float)0;
    }

    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }        

        if(getClass() != o.getClass()) {
            return false;
        }

        Temperature otherObj = (Temperature)o;

        if(TimeStamp != otherObj.TimeStamp) {
            return false;
        }
        if(TSensorID != otherObj.TSensorID) {
            return false;
        }
        if(TValue != otherObj.TValue) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int __result = 0;
        __result += (int)TimeStamp;
        __result += (int)TSensorID;
        __result += (int)TValue;
        return __result;
    }

    /**
    * This is the implementation of the <code>Copyable</code> interface.
    * This method will perform a deep copy of <code>src</code>
    * This method could be placed into <code>TemperatureTypeSupport</code>
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

        Temperature typedSrc = (Temperature) src;
        Temperature typedDst = this;

        typedDst.TimeStamp = typedSrc.TimeStamp;
        typedDst.TSensorID = typedSrc.TSensorID;
        typedDst.TValue = typedSrc.TValue;

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
        strBuffer.append("TSensorID: ").append(TSensorID).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("TValue: ").append(TValue).append("\n");  

        return strBuffer.toString();
    }

}
