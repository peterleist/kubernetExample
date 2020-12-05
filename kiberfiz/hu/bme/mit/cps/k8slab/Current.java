

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

public class Current   implements Copyable, Serializable{

    public long TimeStamp = (long)0;
    public int ISensorID = (int)0;
    public float IValue = (float)0;

    public Current() {

    }
    public Current (Current other) {

        this();
        copy_from(other);
    }

    public static Object create() {

        Current self;
        self = new  Current();
        self.clear();
        return self;

    }

    public void clear() {

        TimeStamp = (long)0;
        ISensorID = (int)0;
        IValue = (float)0;
    }

    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }        

        if(getClass() != o.getClass()) {
            return false;
        }

        Current otherObj = (Current)o;

        if(TimeStamp != otherObj.TimeStamp) {
            return false;
        }
        if(ISensorID != otherObj.ISensorID) {
            return false;
        }
        if(IValue != otherObj.IValue) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int __result = 0;
        __result += (int)TimeStamp;
        __result += (int)ISensorID;
        __result += (int)IValue;
        return __result;
    }

    /**
    * This is the implementation of the <code>Copyable</code> interface.
    * This method will perform a deep copy of <code>src</code>
    * This method could be placed into <code>CurrentTypeSupport</code>
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

        Current typedSrc = (Current) src;
        Current typedDst = this;

        typedDst.TimeStamp = typedSrc.TimeStamp;
        typedDst.ISensorID = typedSrc.ISensorID;
        typedDst.IValue = typedSrc.IValue;

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
        strBuffer.append("ISensorID: ").append(ISensorID).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("IValue: ").append(IValue).append("\n");  

        return strBuffer.toString();
    }

}
