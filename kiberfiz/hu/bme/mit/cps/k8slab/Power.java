

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

public class Power   implements Copyable, Serializable{

    public long TimeStamp = (long)0;
    public float PValue = (float)0;

    public Power() {

    }
    public Power (Power other) {

        this();
        copy_from(other);
    }

    public static Object create() {

        Power self;
        self = new  Power();
        self.clear();
        return self;

    }

    public void clear() {

        TimeStamp = (long)0;
        PValue = (float)0;
    }

    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }        

        if(getClass() != o.getClass()) {
            return false;
        }

        Power otherObj = (Power)o;

        if(TimeStamp != otherObj.TimeStamp) {
            return false;
        }
        if(PValue != otherObj.PValue) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int __result = 0;
        __result += (int)TimeStamp;
        __result += (int)PValue;
        return __result;
    }

    /**
    * This is the implementation of the <code>Copyable</code> interface.
    * This method will perform a deep copy of <code>src</code>
    * This method could be placed into <code>PowerTypeSupport</code>
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

        Power typedSrc = (Power) src;
        Power typedDst = this;

        typedDst.TimeStamp = typedSrc.TimeStamp;
        typedDst.PValue = typedSrc.PValue;

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
        strBuffer.append("PValue: ").append(PValue).append("\n");  

        return strBuffer.toString();
    }

}
