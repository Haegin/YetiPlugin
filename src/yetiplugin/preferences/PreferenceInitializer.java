package yetiplugin.preferences;

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
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import yetiplugin.YetiPlugIn;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	
	public void initializeDefaultPreferences() {

		IPreferenceStore store = YetiPlugIn.getDefault().getPreferenceStore();
		
		//Calling Yeti for given amount of time		
		store.setDefault(PreferenceConstants.CALLING_TIME, 50);		
		store.setDefault(PreferenceConstants.CALL_TIME_CHOICE, true);
		
		store.setDefault(PreferenceConstants.CALL_NOOFTEST_CHOICE,true);
		store.setDefault(PreferenceConstants.CALL_TIME_CHOICE,false);

		store.setDefault(PreferenceConstants.OUTPUTSEVERITY, "Error");

		// for calling Yeti to attempt X method calls.
		store.setDefault(PreferenceConstants.CALLING_ATTEMPT_NOS, 10);
		
		store.setDefault(PreferenceConstants.TESTINGLSTRATEGY,"Random Plus");
		store.setDefault(PreferenceConstants.TIMESCALE, "Seconds");
		store.setDefault(PreferenceConstants.TESTINGLANGAUAGE, "Java");
		
		/*  sets the timeout (in milliseconds) for a method call to X.
		   Note that too low values may result in blocking Yeti (use at least 30ms for good performances). */
		store.setDefault(PreferenceConstants.METHODTIMEOUT, 50);
		store.setDefault(PreferenceConstants.METHODMAKEVISIBLE, true);
		
	    /*  probability to inject new instances
		    at each call (if relevant). Value between 0 and 100.  */ 
		store.setDefault(PreferenceConstants.NEWINSTANCEINJECTIONPROBABILITY, 100);
		   
	    /*-probabilityToUseNullValue=X : probability to use a null instance at
	    each variable (if relevant). Value between 0 and 100 default is 1. */
		store.setDefault(PreferenceConstants.PORBABILITYTOUSENULLVALUE, 100);
		
		/* Removes the cap on the maximum of instances for a given type.
		Default is there is and the max is 1000. */
		store.setDefault(PreferenceConstants.REMOVE_CAP_MAX_OF_INSTANCES, 0);
			 
		//sets the cap on the number of instances for any given type. Defaults is 1000.
		store.setDefault(PreferenceConstants.CAP_NO_OF_INSTANCE, 1000);
		
	}

}
