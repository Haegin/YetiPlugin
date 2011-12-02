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

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import yetiplugin.YetiPlugIn;


public class PreferenceSettingsPage extends PreferencePage implements IWorkbenchPreferencePage{

	private RadioGroupFieldEditor logOption;
	

    private Text			callingAmtTimeText,callingByNoTestText;
    private Combo       	timeScaleCombo,testingLanguageCombo,testStrategyCombo;
    private Combo       	outputSeverityCombo;
    Label 					strategyLabel; 
    Label 					testingLanguageLabel;
    Label 					outputSeverityLabel;
    IntegerFieldEditor 		removeCapMaxOfInstances ;
	IntegerFieldEditor 		capNoOfInstances ;
	IntegerFieldEditor 		newInstanceInjectionProbability;
	IntegerFieldEditor 		nullInstanceEachVariable;
	DirectoryFieldEditor 	yetiPath;
	Button  				makeMethodvisibleButton;
	IntegerFieldEditor 		timeoutMethod;
	Button 					callingamtOfTimeButton;
	Button 					callingByNoOfTestButton;
	IPreferenceStore 		store;
    FileFieldEditor 		outputTraceFilePathField;
    FileFieldEditor 		inputTraceFilePathField ;
    FileFieldEditor 		outputUnitTestFileField ;

	public PreferenceSettingsPage()
	{
		super();
		
		// Set the preference store for the preference page.
		store =YetiPlugIn.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		//setDescription("Yeti");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		
		// Create parent composite
     Composite composite = new Composite(parent, SWT.NONE);
     GridLayout layout = new GridLayout(1, false);
     layout.verticalSpacing = 10;
     composite.setLayout(layout);
		        
        // Create groups
       @SuppressWarnings("unused")
	Group callingGroup 	= buildCallingGroup(composite);
       @SuppressWarnings("unused")
	Group strategy     	= buildStrategyGroup(composite);
       @SuppressWarnings("unused")
	Group instanceGroup = buildInstanceGroup(composite);
       @SuppressWarnings("unused")
	Group methodGroup	= buildMethodGroup(composite);
       @SuppressWarnings("unused")
	Group traceGroup 	= buildTraceGroup(composite);
         return composite;

	}
	
	private Group buildCallingGroup(final Composite parent) {		
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = false;

        GridData gridData1 = new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData11.horizontalSpan = 3;
                     
        Group callingGroup = new Group(parent, SWT.NONE);
        callingGroup.setText("Calling Yeti");
        callingGroup.setLayout(gridLayout);
        
        callingamtOfTimeButton = new Button(callingGroup, SWT.RADIO);
        callingamtOfTimeButton.setText("Allocated testing time:");
        callingamtOfTimeButton.setLayoutData(gridData);
        
        callingamtOfTimeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	if ( callingamtOfTimeButton.getSelection() )
            	{
            		callingAmtTimeText.setEnabled(true);
                	timeScaleCombo.setEnabled(true);
                	callingByNoTestText.setEnabled(false);
                	callingByNoOfTestButton.setSelection(false);
            	}
            	else
            	{
            		callingByNoOfTestButton.setSelection(true);
            	   	callingByNoTestText.setEnabled(true);
            	   	callingAmtTimeText.setEnabled(false);
                	timeScaleCombo.setEnabled(false);
                	
            	}
            }
         });

        
	//	Specifying time for the total test run. Can be specified in 
	//	minutes or seconds
        callingAmtTimeText = new Text(callingGroup, SWT.BORDER  | SWT.RIGHT);
        callingAmtTimeText.setToolTipText("Testing time");
        callingAmtTimeText.setLayoutData(gridData1);
        

        timeScaleCombo = new Combo(callingGroup, SWT.NONE  | SWT.DROP_DOWN | SWT.READ_ONLY);
        timeScaleCombo.add("Minutes", 0);
        timeScaleCombo.add("Seconds", 1);
        
        Label separator = new Label(callingGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(gridData11);

        callingByNoOfTestButton = new Button(callingGroup, SWT.RADIO);
        callingByNoOfTestButton.setText("Number of method calls:");
        callingByNoOfTestButton.setLayoutData(gridData);
        
        callingByNoOfTestButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	if ( callingByNoOfTestButton.getSelection() )
            	{
            	callingamtOfTimeButton.setSelection(false);
            	callingByNoTestText.setEnabled(true);
            	callingAmtTimeText.setEnabled(false);
            	timeScaleCombo.setEnabled(false);
            	}
            	else
            	{
            		callingByNoTestText.setEnabled(false);
            		callingamtOfTimeButton.setSelection(true);
                	callingAmtTimeText.setEnabled(true);
                	timeScaleCombo.setEnabled(true);
            	}
            }
         });


        callingByNoTestText = new Text(callingGroup, SWT.BORDER | SWT.RIGHT);
        callingByNoTestText.setToolTipText("This allows specifying the limit of number of tests cases to be generated in the session.");
        callingByNoTestText.setLayoutData(gridData1);

        initializeCallingValues();
        return callingGroup;
	}
	
	private Group buildStrategyGroup(final Composite parent) {		
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        
          
        Group StrategyGroup = new Group(parent, SWT.NONE);
        StrategyGroup.setText("Testing Settings");
        StrategyGroup.setLayout(gridLayout);
        
        strategyLabel = new Label(StrategyGroup, SWT.NULL);
        strategyLabel.setText("Testing strategy:");

        testStrategyCombo = new Combo(StrategyGroup, SWT.NONE  | SWT.DROP_DOWN | SWT.READ_ONLY);
        testStrategyCombo.add("Random Plus", 0);
        testStrategyCombo.add("Random Plus Periodic", 1);
        testStrategyCombo.add("Random Plus Decreasing", 2);
        
        testingLanguageLabel = new Label(StrategyGroup, SWT.NULL);
        testingLanguageLabel.setText("Testing language:");

        testingLanguageCombo = new Combo(StrategyGroup, SWT.NONE  | SWT.DROP_DOWN | SWT.READ_ONLY);
        testingLanguageCombo.add("Java", 0);
        testingLanguageCombo.add("JML", 1);
        testingLanguageCombo.add("Cofoja", 2);
       
        outputSeverityLabel = new Label(StrategyGroup, SWT.NULL);
        outputSeverityLabel.setText("Marker type:");

        outputSeverityCombo = new Combo(StrategyGroup, SWT.NONE  | SWT.DROP_DOWN | SWT.READ_ONLY);
        outputSeverityCombo.add("Error", 0);
        outputSeverityCombo.add("Warning", 1);
        
        
//    	-branchCoverage : shows the branch coverage if available (in Java, this implies instrumenting the bytecode).
        
        initializeStrategyValues();
        
        return StrategyGroup;
	}


		
	
	private Group buildTraceGroup(final Composite parent) {	
		        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;

		Group traceGroup = new Group(parent, SWT.NONE);
		traceGroup.setText("Path Settings");
		traceGroup.setLayout(gridLayout);

		//-yetiPath=X : stores the path that contains the code to test (e.g. for Java the classpath to consider		      	        
		yetiPath = new DirectoryFieldEditor (PreferenceConstants.YETIPATH, 
				"Stores the path that contains the code to test:", traceGroup);
		
		// The file where to output traces on disk		      	        
		 outputTraceFilePathField=	new FileFieldEditor (PreferenceConstants.OUTPUTFILEPATH, 
				"Output traces file path:", traceGroup);

		 // The files where to input traces from disk (file names separated by ':').
		 inputTraceFilePathField = new FileFieldEditor (PreferenceConstants.INPUTFILEPATH, 
				"Input traces file path:", traceGroup);
		
		 outputUnitTestFileField = new FileFieldEditor (PreferenceConstants.OUTPUTUNITTESTFILE, 
					"Testcase output filename:", traceGroup); 
			 
		initializeTraceValues();
		return traceGroup;

	}

	
	private Group buildInstanceGroup(final Composite parent) {	
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;

        
		Group instanceGroup = new Group(parent, SWT.VERTICAL);
		instanceGroup.setText("Instance Settings");
		instanceGroup.setLayout(gridLayout);
		
		
//	    -noInstancesCap : removes the cap on the maximum of instances for a given type. Default is there is and the max is 1000.
	  	 removeCapMaxOfInstances = new IntegerFieldEditor(
					PreferenceConstants.REMOVE_CAP_MAX_OF_INSTANCES,
					"Maximum number of instances:",
					instanceGroup);
	  	removeCapMaxOfInstances.setValidRange(0, 1000);
	  	
	  	
					
	  
//      -instancesCap=X : sets the cap on the number of instances for any given type. Defaults is 1000.
		 capNoOfInstances = new IntegerFieldEditor(
				PreferenceConstants.CAP_NO_OF_INSTANCE,
				"Sets the cap on the number of instances:",
				instanceGroup);
		 
			
		//-newInstanceInjectionProbability=X : probability to inject new instances at each call (if relevant). Value between 0 and 100.
		  newInstanceInjectionProbability = new IntegerFieldEditor(
					PreferenceConstants.NEWINSTANCEINJECTIONPROBABILITY,
					"New instance probability:",
					instanceGroup);
		   newInstanceInjectionProbability.setValidRange(0, 100);
								
				
	//-probabilityToUseNullValue=X : probability to use a null instance at each variable (if relevant). Value between 0 and 100 default is 1.
		   nullInstanceEachVariable = new IntegerFieldEditor(
					PreferenceConstants.PORBABILITYTOUSENULLVALUE,
					"Null instance probability:",
					instanceGroup);
		   nullInstanceEachVariable.setValidRange(0, 100);
		   
		initializeInstanceValues();	
		return instanceGroup;

	}

	private Group buildMethodGroup(final Composite parent) {	
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        
		Group methodGroup = new Group(parent, SWT.NONE);
		methodGroup.setText("Method Settings");
		methodGroup.setLayout(gridLayout);
		
		// -msCalltimeout=X : sets the timeout (in milliseconds) for a method call to X.
		timeoutMethod = new IntegerFieldEditor(
					PreferenceConstants.METHODTIMEOUT,
					"Sets the timeout (in milliseconds) for a method:",
					methodGroup);
		
        
		
		makeMethodvisibleButton = new Button(methodGroup, SWT.CHECK);
		makeMethodvisibleButton.setText("Make method visible:");
	        		 
		 initializeMethodValues();
		 
		return methodGroup;
	}

	private void initializeCallingValues()
	{
		callingAmtTimeText.setText(store.getString(PreferenceConstants.CALLING_TIME));
		callingamtOfTimeButton.setSelection(store.getBoolean(PreferenceConstants.CALL_TIME_CHOICE));
		callingByNoTestText.setText(store.getString(PreferenceConstants.CALLING_ATTEMPT_NOS));
		callingByNoOfTestButton.setSelection(store.getBoolean(PreferenceConstants.CALL_NOOFTEST_CHOICE));
		timeScaleCombo.setText(store.getString(PreferenceConstants.TIMESCALE));
		
		if ( callingByNoOfTestButton.getSelection() )
    	{
    	callingamtOfTimeButton.setSelection(false);
    	callingByNoTestText.setEnabled(true);
    	callingAmtTimeText.setEnabled(false);
    	timeScaleCombo.setEnabled(false);
    	}
    	else
    	{
    		callingByNoTestText.setEnabled(false);
    		callingamtOfTimeButton.setSelection(true);
        	callingAmtTimeText.setEnabled(true);
        	timeScaleCombo.setEnabled(true);
    	}
	} 
	    
	
	private void initializeInstanceValues()
	{
		removeCapMaxOfInstances.setStringValue(store.getString(PreferenceConstants.REMOVE_CAP_MAX_OF_INSTANCES));
		capNoOfInstances.setStringValue(store.getString(PreferenceConstants.CAP_NO_OF_INSTANCE));
		newInstanceInjectionProbability.setStringValue(store.getString(PreferenceConstants.NEWINSTANCEINJECTIONPROBABILITY));
		nullInstanceEachVariable.setStringValue(store.getString(PreferenceConstants.PORBABILITYTOUSENULLVALUE));
	}
	
	private void initializeTraceValues()
	{
		yetiPath.setStringValue(store.getString(PreferenceConstants.YETIPATH));
		outputTraceFilePathField.setStringValue(store.getString(PreferenceConstants.OUTPUTFILEPATH));
		inputTraceFilePathField.setStringValue(store.getString(PreferenceConstants.INPUTFILEPATH));		
		outputUnitTestFileField.setStringValue(store.getString(PreferenceConstants.OUTPUTUNITTESTFILE));	
	}
	
	
	private void initializeMethodValues()
	{
		timeoutMethod.setStringValue(store.getString(PreferenceConstants.METHODTIMEOUT));
		makeMethodvisibleButton.setSelection(store.getBoolean(PreferenceConstants.METHODMAKEVISIBLE));
	}	
	
	private void initializeStrategyValues()
	{
		testStrategyCombo.setText(store.getString(PreferenceConstants.TESTINGLSTRATEGY));
		testingLanguageCombo.setText(store.getString(PreferenceConstants.TESTINGLANGAUAGE));
		outputSeverityCombo.setText(store.getString(PreferenceConstants.OUTPUTSEVERITY));
	}
	   
	@Override
	public void init(IWorkbench workbench) {	}
	
	/*
	 * The user has pressed "Restore defaults".
	 * Restore all default preferences.
	 */
	protected void performDefaults() {
		
		logOption.loadDefault();
		removeCapMaxOfInstances.loadDefault();
		capNoOfInstances.loadDefault();
		newInstanceInjectionProbability.loadDefault();
		nullInstanceEachVariable.loadDefault();
		yetiPath.loadDefault();
		timeoutMethod.loadDefault();
//		printNoOfCallsPerMethod.loadDefault();
		inputTraceFilePathField.loadDefault();
		outputTraceFilePathField.loadDefault();
		
		super.performDefaults();
	}
	
	/*
	 * The user has pressed Ok or Apply. Store/apply 
	 * this page's values appropriately.
	 */	
	public boolean performOk() {
			
		IPreferenceStore store = getPreferenceStore();
		
		if ( callingamtOfTimeButton.getSelection() == false && callingByNoOfTestButton.getSelection()==false)
		{
			return false;
		}
		
		store.setValue(PreferenceConstants.CALLING_TIME, callingAmtTimeText.getText());
		store.setValue(PreferenceConstants.CALLING_ATTEMPT_NOS, callingByNoTestText.getText());
		store.setValue(PreferenceConstants.CALL_TIME_CHOICE, callingamtOfTimeButton.getSelection());
		store.setValue(PreferenceConstants.CALL_NOOFTEST_CHOICE, callingByNoOfTestButton.getSelection());

		store.setValue(PreferenceConstants.TIMESCALE, timeScaleCombo.getText());
		store.setValue(PreferenceConstants.TESTINGLANGAUAGE, testingLanguageCombo.getText());
		store.setValue(PreferenceConstants.TESTINGLSTRATEGY, testStrategyCombo.getText());
		store.setValue(PreferenceConstants.OUTPUTSEVERITY, outputSeverityCombo.getText());
		
		store.setValue(PreferenceConstants.METHODTIMEOUT, timeoutMethod.getStringValue());	
		store.setValue(PreferenceConstants.METHODMAKEVISIBLE, makeMethodvisibleButton.getSelection());
		
		store.setValue(PreferenceConstants.REMOVE_CAP_MAX_OF_INSTANCES, removeCapMaxOfInstances.getStringValue());
		store.setValue(PreferenceConstants.CAP_NO_OF_INSTANCE, capNoOfInstances.getStringValue());
		store.setValue(PreferenceConstants.NEWINSTANCEINJECTIONPROBABILITY, newInstanceInjectionProbability.getStringValue());
		store.setValue(PreferenceConstants.PORBABILITYTOUSENULLVALUE, nullInstanceEachVariable.getStringValue());
		
		store.setValue(PreferenceConstants.YETIPATH, yetiPath.getStringValue());
		store.setValue(PreferenceConstants.OUTPUTFILEPATH, outputTraceFilePathField.getStringValue());
		store.setValue(PreferenceConstants.INPUTFILEPATH, inputTraceFilePathField.getStringValue());
		store.setValue(PreferenceConstants.OUTPUTUNITTESTFILE, outputUnitTestFileField.getStringValue());	
	//	store.setValue(PreferenceConstants.BRANCH_COVERAGE, branchCoverage.getBooleanValue());
		
		return super.performOk();
	}
	
	

}
