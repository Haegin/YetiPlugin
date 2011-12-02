package yetiplugin;


/**

YETI - York Extensible Testing Infrastructure

Copyright (c) 2009-2010, Manuel Oriol <manuel.oriol@gmail.com> - University of York
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
must display the following acknowledgement:
This product includes software developed by the University of York.
4. Neither the name of the University of York nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Manuel Oriol <manuel.oriol@gmail.com> ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

**/
import org.eclipse.jface.preference.IPreferenceStore;
import yetiplugin.preferences.PreferenceConstants;

public class Util {
	public static  StringBuilder getYetiPreferenceValue(String testModule , String classpath) 
	{					
		StringBuilder referenceParameter = new StringBuilder();	
		YetiPlugIn.getSummaryInfoArrayList().addSummary("Yeti Testing Started");
		YetiPlugIn.getSummaryInfoArrayList().addSummary("Yeti Testing Parameters");
		
			IPreferenceStore store = YetiPlugIn.getDefault().getPreferenceStore(); 
			if ( store.getString(PreferenceConstants.TESTINGLANGAUAGE).equals("Java") )
				{ 
				referenceParameter  = referenceParameter.append("-java -testModules=");
				}
			else if ( store.getString(PreferenceConstants.TESTINGLANGAUAGE).equals("JML") )
				{
					referenceParameter  = referenceParameter.append("-JML -testModules=");
				}
				else if ( store.getString(PreferenceConstants.TESTINGLANGAUAGE).equals("Cofoja"))
				{
					referenceParameter  = referenceParameter.append("-Cofoja -testModules=");
				}
			
			referenceParameter  = referenceParameter.append(testModule.trim());
			YetiPlugIn.getSummaryInfoArrayList().addSummary("Testing Module Name : " + testModule.trim());
			
			if ( store.getBoolean(PreferenceConstants.CALL_TIME_CHOICE))
			{
				if ( store.getString(PreferenceConstants.TIMESCALE).equals("Seconds") )
				{
					referenceParameter = referenceParameter.append(" -time="+store.getString(PreferenceConstants.CALLING_TIME)+"s");
					YetiPlugIn.getSummaryInfoArrayList().addSummary("Specified time for the total test run:"+store.getString(PreferenceConstants.CALLING_TIME)+"Seconds");
				}
				else
				{
					referenceParameter = referenceParameter.append(" -time="+store.getString(PreferenceConstants.CALLING_TIME)+"mn");
					YetiPlugIn.getSummaryInfoArrayList().addSummary("Specified time for the total test run:"+store.getString(PreferenceConstants.CALLING_TIME)+"Minutes");
				}
			}	
		
			//This allows specifying the limit of number of tests cases to be 
			//generated in the session
			if ( store.getBoolean(PreferenceConstants.CALL_NOOFTEST_CHOICE) )
			{
				referenceParameter = referenceParameter.append(" -nTests="+store.getString(PreferenceConstants.CALLING_ATTEMPT_NOS));
				YetiPlugIn.getSummaryInfoArrayList().addSummary("Specified the limit of No. of test case to be generated in the session: " + store.getString(PreferenceConstants.CALLING_ATTEMPT_NOS));
			}					
	 	     // Call from Plugin
	 	     referenceParameter = referenceParameter.append(" -plugin");
		 	 if ( store.getInt(PreferenceConstants.CAP_NO_OF_INSTANCE) > 0)  
		 	 {   
		 		 referenceParameter = referenceParameter.append(" -instancesCap="+store.getString(PreferenceConstants.CAP_NO_OF_INSTANCE));
		 		 YetiPlugIn.getSummaryInfoArrayList().addSummary("Maximum of instances for a given type:"+store.getString(PreferenceConstants.CAP_NO_OF_INSTANCE));
		 	 }		 	 	 
		 	if (store.getBoolean(PreferenceConstants.METHODMAKEVISIBLE))
		 	{
		 		 referenceParameter = referenceParameter.append(" -makeMethodsVisible");
			 	 YetiPlugIn.getSummaryInfoArrayList().addSummary("Converts all the protected and private methods into public for testing."+store.getBoolean(PreferenceConstants.METHODMAKEVISIBLE));
		 	}
		 	else
		 	{
		 		 YetiPlugIn.getSummaryInfoArrayList().addSummary("Converts all the protected and private methods into public for testing."+store.getBoolean(PreferenceConstants.METHODMAKEVISIBLE));
		 	}
	 	    
		 		referenceParameter = referenceParameter.append(" -yetiPath="+classpath);
	 	    	YetiPlugIn.getSummaryInfoArrayList().addSummary("Yeti Path :"+classpath);
	 	    
	 	    	
	 	    	// -tracesInputFiles=X : the files where to input traces from disk (file names separated by ':').
		    if ( ! store.getString(PreferenceConstants.INPUTFILEPATH).trim().equals(""))
		    {	
		    	referenceParameter = referenceParameter.append(" -tracesInputFiles="+store.getString(PreferenceConstants.INPUTFILEPATH));
		    	YetiPlugIn.getSummaryInfoArrayList().addSummary("Trace input File:"+store.getString(PreferenceConstants.INPUTFILEPATH));
		    }
		    if ( ! store.getString(PreferenceConstants.OUTPUTFILEPATH).trim().equals(""))
		 	{
		    	referenceParameter = referenceParameter.append(" -tracesOutputFile="+store.getString(PreferenceConstants.OUTPUTFILEPATH));
		    	YetiPlugIn.getSummaryInfoArrayList().addSummary("Trace Output File:"+store.getString(PreferenceConstants.OUTPUTFILEPATH));
		 	}
		    if ( ! store.getString(PreferenceConstants.OUTPUTUNITTESTFILE).trim().equals(""))
	 	    {
		    	referenceParameter = referenceParameter.append(" -outputUnitTestFile="+store.getString(PreferenceConstants.OUTPUTUNITTESTFILE));
		    	YetiPlugIn.getSummaryInfoArrayList().addSummary("Output unit test case file name:"+store.getString(PreferenceConstants.YETIPATH));
	 	     }
			return referenceParameter;
	}
}
