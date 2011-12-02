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

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {
	
	//Calling Yeti for given amount of time
	public static final String CALLING_TIME = "CallingTime";
	public static final String CALL_TIME_CHOICE = "CallingByTime";
	public static final String TIMESCALE = "TimeScale";
	public static final String CALLING_ATTEMPT_NOS = "CallingAttemptNos";
	public static final String CALL_NOOFTEST_CHOICE = "CallingByNoOfTest";
	
	// Testing Settings
	
	public static final String TESTINGLANGAUAGE = "TestingLanguage";
	public static final String TESTINGLSTRATEGY = "TestingStrategy";
	public static final String OUTPUTSEVERITY = "OutputSeverity";
	public static final String AUTOGENERATIONTESTCASE = "AutoGenerationTestCase";
	public static final String NUMBEROFCALLSPERMETHOD = "NumberOfCallsPerMethod";	
      
	// Instance Settings
	public static final String REMOVE_CAP_MAX_OF_INSTANCES = "removeCapMaxOfInstances"; 
	public static final String CAP_NO_OF_INSTANCE = "capNoOfInstances";	   
   	public static final String NEWINSTANCEINJECTIONPROBABILITY = "newInstanceInjectionProbability";   
	public static final String PORBABILITYTOUSENULLVALUE = "probabilityToUseNullValue";   

	// Method Settings
   	public static final String METHODTIMEOUT = "Timeout";
   	//converts all the protected and private methods into public for testing
   	public static final String METHODMAKEVISIBLE = "MethodMakeVisible";
   	
   	// Path settings
	public static final String  YETIPATH = "yetiPath";
	public static final String OUTPUTFILEPATH = "outputFilePath";
	public static final String INPUTFILEPATH = "inputFilePath";
	public static final String OUTPUTUNITTESTFILE = "OutputUnitTestFile";
  		

}
