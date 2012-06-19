/*******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2012 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.pgpencryptstream;

import java.util.List;
import java.util.Map;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.shared.SharedObjectInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

/*
 * Created on 03-Juin-2008
 * 
 */

public class PGPEncryptStreamMeta extends BaseStepMeta implements StepMetaInterface
{
	private static Class<?> PKG = PGPEncryptStreamMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$

	/** GPG location	*/
	private String gpglocation;
	
	/** Key name  **/
	private String keyname;
	
    /** dynamic stream filed */
    private String       streamfield;
    
    /** function result: new value name */
    private String       resultfieldname;
    
    public PGPEncryptStreamMeta()
    {
        super(); // allocate BaseStepMeta
    }
    
    public void setGPGPLocation(String gpglocation)
    {
 	   this.gpglocation=gpglocation;
    }
    
    public String getGPGLocation()
    {
 	   return gpglocation;
    }
    /**
     * @return Returns the streamfield.
     */
    public String getStreamField()
    {
        return streamfield;
    }

    /**
     * @param streamfield The streamfield to set.
     */
    public void setStreamField(String streamfield)
    {
        this.streamfield = streamfield;
    }

    /**
     * @return Returns the resultName.
     */
    public String getResultFieldName()
    {
        return resultfieldname;
    }


    /**
     * @param resultfieldname The resultfieldname to set.
     */
    public void setResultfieldname(String resultfieldname)
    {
        this.resultfieldname = resultfieldname;
    }

    /**
     * @return Returns the keyname.
     */
    public String getKeyName()
    {
        return keyname;
    }


    /**
     * @param keyname The keyname to set.
     */
    public void setKeyName(String keyname)
    {
        this.keyname = keyname;
    }

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleXMLException
	{
		readData(stepnode, databases);
	}
 

    public Object clone()
    {
        PGPEncryptStreamMeta retval = (PGPEncryptStreamMeta) super.clone();
       
        return retval;
    }

    public void setDefault()
    {
        resultfieldname = "result"; //$NON-NLS-1$
        streamfield=null;
        keyname=null;
        gpglocation=null;
    }
	
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface info[], StepMeta nextStep, VariableSpace space) throws KettleStepException
	{    	
        // Output fields (String)
		 if (!Const.isEmpty(resultfieldname))
	     {
			 ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(resultfieldname), ValueMeta.TYPE_STRING);
			 v.setOrigin(name);
			 inputRowMeta.addValueMeta(v);
	     }
		 
    }

    public String getXML()
    {
        StringBuffer retval = new StringBuffer();
        retval.append("    " + XMLHandler.addTagValue("gpglocation", gpglocation));
        retval.append("    " + XMLHandler.addTagValue("keyname", keyname));
        retval.append("    " + XMLHandler.addTagValue("streamfield", streamfield)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    " + XMLHandler.addTagValue("resultfieldname", resultfieldname)); //$NON-NLS-1$ //$NON-NLS-2$
        return retval.toString();
    }

    private void readData(Node stepnode, List<? extends SharedObjectInterface> databases)
	throws KettleXMLException
	{
    	try
    	{
    		gpglocation = XMLHandler.getTagValue(stepnode, "gpglocation");
    		keyname = XMLHandler.getTagValue(stepnode, "keyname");
			streamfield = XMLHandler.getTagValue(stepnode, "streamfield"); //$NON-NLS-1$
            resultfieldname = XMLHandler.getTagValue(stepnode, "resultfieldname"); 
        }
        catch (Exception e)
        {
            throw new KettleXMLException(BaseMessages.getString(PKG, "PGPEncryptStreamMeta.Exception.UnableToReadStepInfo"), e); //$NON-NLS-1$
        }
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleException
	{
    	try
		{
    		gpglocation = rep.getStepAttributeString(id_step, "gpglocation");
    		keyname = rep.getStepAttributeString(id_step, "keyname");
    		streamfield = rep.getStepAttributeString(id_step, "streamfield"); //$NON-NLS-1$
            resultfieldname = rep.getStepAttributeString(id_step, "resultfieldname"); //$NON-NLS-1$ 
        }
        catch (Exception e)
        {
            throw new KettleException(BaseMessages.getString(PKG, "PGPEncryptStreamMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
        }
    }

    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException
    {
        try
        {
        	rep.saveStepAttribute(id_transformation, id_step, "gpglocation", gpglocation);
        	rep.saveStepAttribute(id_transformation, id_step, "keyname", keyname);
            rep.saveStepAttribute(id_transformation, id_step, "streamfield", streamfield); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "resultfieldname", resultfieldname); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            throw new KettleException(BaseMessages.getString(PKG, "PGPEncryptStreamMeta.Exception.UnableToSaveStepInfo") + id_step, e); //$NON-NLS-1$
        }
    }

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
        CheckResult cr;
        String error_message = ""; //$NON-NLS-1$

      

        if (Const.isEmpty(gpglocation))
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.GPGLocationMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.GPGLocationOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
        }
        if (Const.isEmpty(keyname))
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.KeyNameMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.KeyNameOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
        }
          
        if (Const.isEmpty(resultfieldname))
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.ResultFieldMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.ResultFieldOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
            remarks.add(cr);
        }
        if (Const.isEmpty(streamfield))
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.StreamFieldMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.StreamFieldOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
            remarks.add(cr);
        }
        // See if we have input streams leading to this step!
        if (input.length > 0)
        {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.ReceivingInfoFromOtherSteps"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }
        else
        {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(PKG, "PGPEncryptStreamMeta.CheckResult.NoInpuReceived"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }

    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
    {
        return new PGPEncryptStream(stepMeta, stepDataInterface, cnr, transMeta, trans);
    }

    public StepDataInterface getStepData()
    {
        return new PGPEncryptStreamData();
    }

    public boolean supportsErrorHandling()
    {
        return true;
    }

}