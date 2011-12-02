package yeti;

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

 THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jgap.IChromosome;
import yeti.environments.YetiInitializer;
import yeti.environments.YetiLoader;
import yeti.environments.YetiProgrammingLanguageProperties;
import yeti.environments.YetiTestManager;
import yeti.environments.cofoja.YetiCoFoJaPrefetchingLoader;
import yeti.environments.commandline.YetiCLInitializer;
import yeti.environments.commandline.YetiCLLogProcessor;
import yeti.environments.commandline.YetiCLProperties;
import yeti.environments.commandline.YetiCLTestManager;
import yeti.environments.csharp.YetiCsharpInitializer;
import yeti.environments.csharp.YetiCsharpLogProcessor;
import yeti.environments.csharp.YetiCsharpProperties;
import yeti.environments.csharp.YetiCsharpTestManager;
import yeti.environments.csharp.YetiServerSocket;
import yeti.environments.java.YetiJavaInitializer;
import yeti.environments.java.YetiJavaLogProcessor;
import yeti.environments.java.YetiJavaMethod;
import yeti.environments.java.YetiJavaPrefetchingLoader;
import yeti.environments.java.YetiJavaProperties;
import yeti.environments.java.YetiJavaTestManager;
import yeti.environments.jml.YetiJMLPrefetchingLoader;
import yeti.environments.pharo.YetiPharoCommunicator;
import yeti.environments.pharo.YetiPharoTestManager;
import yeti.monitoring.YetiGUI;
import yeti.stats.YetiDataSet;
import yeti.stats.YetiMichaelisMentenEquation;
import yeti.strategies.GA.*;
import yeti.strategies.YetiDSSRStrategy;
import yeti.strategies.YetiRandomPlusDecreasing;
import yeti.strategies.YetiRandomPlusStrategy;
import yeti.strategies.YetiRandomPlusPeriodicProbabilitiesStrategy;
import yeti.strategies.YetiRandomStrategy;
import yeti.cloud.YetiMap;
import yetiplugin.YetiPlugIn;
import yetiplugin.marker.Marker;
import yetiplugin.preferences.PreferenceConstants;
import yetiplugin.views.ViewGraph;
import yeti.YetiType;

/**
 * Class that represents the main launching class of Yeti
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jun 22, 2009
 */
public class Yeti {

	/**
	 * The report for the testing session.
	 */
	public static YetiReport report= null;

	/**
	 * The chromosome to run the evolutionary stratgy
	 */
	public static IChromosome chromosome;
	/**
	 * The main gui for Yeti.
	 */
	public static YetiGUI gui =null;

	/**
	 * The properties of the programming language.
	 */
	public static YetiProgrammingLanguageProperties pl;

	/**
	 * The strategy being used.
	 */
	public static YetiStrategy strategy = null;

	/**
	 * The tested modules.
	 */
	public static YetiModule testModule = null; //combined form
	public static String[] testModulesName = null; //string format names
	/**
	 * The mode of operation 
	 */
	public static boolean isDistributed= false;
	/**
	 * The support for branch coverage. 
	 */
	public static boolean hasBranchCoverage= false;

	/**
	 * The support for branch coverage. 
	 */
	public static boolean makeMethodsVisible = false;


	/**
	 * Stores the path to use for testing.
	 */
	public static String yetiPath = ".";//System.getProperty("java.class.path");


	/**
	 * Starting testing time.
	 */
	public static long st;


	/**
	 * The report for the last session.
	 */
	public static YetiReport rep=null;

	/**
	 * Number of test cases executed. 
	 */
	public static long testCaseCount=0;

	/**
	 * The engine used to make tests.
	 */
	public static YetiEngine engine;

	public static boolean compactReport=false;

	public static boolean finished=false;

	/**
	 * The generated test cases once processed. 
	 */
	public static Vector<String> processedTestCases;
	/**
	 * Main method of Yeti. It serves YetiRun the arguments it receives.
	 * Arguments are numerous. Here is a list of the current ones:
	 * <br>
	 * <br>
	 * -help, -h: prints the help out.<br>
	 * -java, -Java : for calling it on Java.<br>
	 * -jml, -JML : for calling it on JML annotated code.<br>
	 * -cofoja, -CoFoJa : for calling it on Java programs annotated with CoFoJa. Note that if you do not want to pre-process the classes statically, you should call Yeti using the option -javaagent:cofoja.jar (or any other path to a CoFoJa jar).<br>
	 * -dotnet, -DOTNET : for calling it on .NET assemblies developed with Code-Contracts.<br>
	 * -time=Xs, -time=Xmn : for calling Yeti for a given amount of time (X can be minutes or seconds, e.g. 2mn or 3s ).<br>
	 * -nTests=X : for calling Yeti to attempt X method calls.<br>
	 * -testModules=M1:M2:...:Mn : for testing one or several modules.Sub-packages of a system can also be specified with asteriks e.g. yeti.test.* will include all the classes in yeti.test + all the classes belonging to the sub-packages of yeti.test .<br>
	 * -initClass=X : this will use a user class to initialize the system this class will be a subclass of yeti.environments.YetiInitializer .<br>
	 * -outputUnitTestFile=X : this option stores the generated test cases in a file. THis is binding-specific for Java: X=tests/test0.T.java will store a file T.java from package test0 into the directory tests.<br>
	 * -rawlogs : prints the logs directly instead of processing them at the end. <br>
	 * -nologs : does not print logs, only the final result.<br>
	 * -msCalltimeout=X : sets the timeout (in milliseconds) for a method call to X. Note that too
	 * low values may result in blocking Yeti (use at least 30ms for good performances).<br>
	 * -yetiPath=X : stores the path that contains the code to test (e.g. for Java the classpath to consider)<br>
	 * -newInstanceInjectionProbability=X : probability to inject new instances at each call (if relevant). Value between 0 and 100. <br>
	 * -probabilityToUseNullValue=X : probability to use a null instance at each variable (if relevant). Value between 0 and 100 default is 1.<br>
	 * -randomPlus : uses the random+ strategy that injects interesting values every now and then.<br>
	 * -randomPlusPeriodic : uses the random+ strategy and periodically change the values of the standard probalilities (null values, new instances, interesting values).<br>
	 * -randomPlusDecreasing : uses the random+ strategy and decreases the values of the standard probalilities (null values, new instances, interesting values).<br>
	 * -DSSR : uses the Dirt Spot Sweeping Random Strategy which extends the random plus strategy and check the boundries around the veriables that found bugs.<br>
	 * -gui : shows the standard graphical user interface for monitoring yeti.<br>
	 * -noInstancesCap : removes the cap on the maximum of instances for a given type. Default is there is and the max is 1000.<br>
	 * -instancesCap=X : sets the cap on the number of instances for any given type. Defaults is 1000.<br>
	 * -tracesOutputFile=X : the file where to output traces on disk<br>
	 * -tracesInputFiles=X : the files where to input traces from disk (file names separated by ':').<br>
	 * -printNumberOfCallsPerMethod : prints the number of calls per method.<br>
	 * -branchCoverage : shows the branch coverage if available (in Java, this implies instrumenting the bytecode)."); <br>
	 * -makeMethodsVisible: converts all the protected and private methods into public for testing.
	 * -approximate : approximates the number of unique failures per number of tests.
	 * -compactReport=X : adds a line in file X containing the information about the testing session.
	 * @param args the arguments of the program
	 */
	public static void main (String[] args) {
		try {
			Yeti.YetiRun(args);
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Please check your options.");
			printHelp();
			//e.printStackTrace();
		}
		if (Yeti.compactReport||YetiGUI.closed) System.exit(0);
		Yeti.finished = true;
	}

	/**
	 * The Run Method for Yeti.
	 * This will receive the same arguments as described for method main and process them
	 * @param args the list of arguments passed on either by main or Map Method in YetiMap
	 */	
	public static void YetiRun(String[] args){
		YetiInitializer secondaryInitializer = null;
		boolean isJava = false;
		boolean isJML = false;
		boolean isCoFoJa = false;
		boolean isDotNet = false;
		boolean isCommandLine = false;
		boolean isPharo = false;
		boolean isTimeout = false;
		int timeOutSec=0;
		boolean isNTests = false;
		boolean isRawLog = false;
		boolean isNoLogs = false;
		boolean isRandomPlus = false;
		boolean isRandomPlusPeriodic = false;
		boolean isRandomPlusDecreasing = false;
		boolean isDSSR = false;
		boolean isEvolutionary = false;
		boolean isRunningFromChromosome = false;		
		String chromosomePath = null;
		boolean showMonitoringGui = false;
		boolean printNumberOfCallsPerMethod = false;
		boolean approximate = false;
		boolean saveInUnitTestFile = false;
		String unitTestFileName = null;
		String modulesString = null;
		//Dhana for plugin
		boolean isPlugin = false;
		
		YetiGAParameters gaParameters = new YetiGAParameters();

		int nTests=0;
		String []modulesToTest=null;
		int callsTimeOut=75;
		Thread th = null;
		String reportFile = null;
		String tracesOutputFile = null;
		String[] traceInputFiles = null;
		YetiLogProcessor logProcessor = null;

		// we parse all arguments of the program
		for (String s0: args) {

			// way of passing options to the language-specific bindings
			// all options should start by "-opt"
			if (s0.startsWith("-opt")) {
				continue;
			}

			// if it is printing help
			if (s0.equals("-help")||s0.equals("-h")) {
				Yeti.printHelp();
				return;
			}
			// if Java
			if (s0.equals("-java")||s0.equals("-Java")) {
				isJava = true;
				continue;
			}
			// if JML
			if (s0.toLowerCase().equals("-jml")) {
				isJML = true;
				continue;
			}

			// if JML
			if (s0.toLowerCase().equals("-cofoja")) {
				isCoFoJa = true;
				continue;
			}
			//if .NET
			if(s0.toLowerCase().equals("-dotnet")){		
				isDotNet = true;
				continue;
			}

			//if command-line
			if(s0.toLowerCase().equals("-cl")){		
				isCommandLine = true;
				continue;
			}

			// if Pharo
			if (s0.equals("-Pharo")) {
				isPharo = true;
				continue;
			}


			// if testing for time value
			if (s0.startsWith("-time=")) {
				isTimeout=true;
				int size = s0.length();
				//GA set evaluation time
				gaParameters.setGaEvaluationTime(s0.substring(6, size));
				// if the time value is in seconds
				if (s0.substring(size-1).equals("s")) {
					timeOutSec=(Integer.parseInt(s0.substring(6, size-1)));
					continue;
				}
				// if the time value is in minutes
				if (s0.substring(size-2).equals("mn")) {
					timeOutSec=60*(Integer.parseInt(s0.substring(6, size-2)));
					continue;	
				}				
			}

			// if testing for time value
			if (s0.startsWith("-msCalltimeout=")) {
				int size = s0.length();
				// if the time value is in seconds
				callsTimeOut=(Integer.parseInt(s0.substring(15, size)));
				if (callsTimeOut<=0) {
					Yeti.printHelp();
					return;
				}
				continue;
			}
			// if testing for new instance injection probability
			if (s0.startsWith("-newInstanceInjectionProbability=")) {
				int size = s0.length();
				// if the time value is in seconds
				YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY=(Integer.parseInt(s0.substring(33, size)))/100d;
				if ((YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY>1.0)||(YetiStrategy.NEW_INSTANCES_INJECTION_PROBABILITY<0)) {
					Yeti.printHelp();
					return;
				}
				continue;
			}

			// if testing for new instance injection probability
			if (s0.startsWith("-probabilityToUseNullValue=")) {
				int size = s0.length();
				// if the time value is in seconds
				YetiVariable.PROBABILITY_TO_USE_NULL_VALUE=((double)Integer.parseInt(s0.substring(27, size)))/100d;
				if ((YetiVariable.PROBABILITY_TO_USE_NULL_VALUE>1.0)||(YetiVariable.PROBABILITY_TO_USE_NULL_VALUE<0)) {
					Yeti.printHelp();
					return;
				}
				continue;
			}

			// if it is for a number of tests
			if (s0.startsWith("-nTests=")) {
				isNTests=true;
				nTests=(Integer.parseInt(s0.substring(8)));
				continue;
			}

			// if collect branch coverage 
			if (s0.startsWith("-branchCoverage")) {
				hasBranchCoverage = true;
				continue;
			}
			// if convert all methods to public
			if (s0.startsWith("-makeMethodsVisible")) {
				makeMethodsVisible = true;
				continue;
			}
			// we want to test these modules
			if (s0.startsWith("-testModules=")) {
				modulesString=s0.substring(13);
				gaParameters.setGaEvaluationModules(modulesString);
				modulesToTest=modulesString.split(":");
				testModulesName = modulesToTest;
				continue;
			}
			// we want to have only logs in standard form
			if (s0.equals("-rawlogs")) {
				isRawLog = true;
				continue;	
			}

			// we want to have only logs in standard form
			if (s0.equals("-nologs")) {
				isNoLogs = true;
				continue;	
			}

			// we want to have only logs in standard form
			if (s0.equals("-gui")) {
				showMonitoringGui = true;
				continue;	
			}

			// we want to use the following path
			if (s0.startsWith("-yetiPath=")) {				

				String s1=s0.substring(10);
				Yeti.yetiPath = s1;
				System.setProperty("java.class.path", System.getProperty("java.class.path")+":"+s1);

				continue;
			}

			// we can use the randomPlus strategy
			if (s0.equals("-randomPlus")) {
				isRandomPlus = true;
				continue;	
			}
			// we can use the randomPlusPeriodic strategy
			if (s0.equals("-randomPlusPeriodic")) {
				isRandomPlusPeriodic = true;
				continue;	
			}			
			// we can use the randomPlusDecreasing strategy
			if (s0.equals("-randomPlusDecreasing")) {
				isRandomPlusDecreasing = true;
				continue;	
			}			

			// we can use the Dirt Spot Sweeping strategy
			if (s0.equals("-DSSR")) {
				isDSSR = true;
				continue;	
			}

			if (s0.startsWith("-evolutionary=")) {
				String s1=s0.substring(14);
				gaParameters.setGaFittestChromosomeOutPutPath(s1);
				isRunningFromChromosome = true;

				isEvolutionary = true;
				continue;
			}

			// we want to use the following path
			if (s0.startsWith("-chromosome=")) {
				String s1=s0.substring(12);
				chromosomePath = s1;
				isRunningFromChromosome = true;
				continue;
			}

			// we have no limits for the number of instances
			if (s0.equals("-noInstancesCap")) {
				YetiType.TYPES_HAVEMAXIMUM_NUMBER_OF_INSTANCES = true;
				continue;	
			}


			// we read a new limit for the number of instances (default is 1000)
			if (s0.startsWith("-instancesCap=")) {
				YetiType.DEFAULT_MAXIMUM_NUMBER_OF_INSTANCES=(Integer.parseInt(s0.substring(14)));
				continue;	
			}

			//setting up the secondary initializer
			if(s0.startsWith("-initClass=")){
				try {
					secondaryInitializer= (YetiInitializer)Yeti.class.getClassLoader().loadClass(s0.substring(11)).newInstance();
				} catch (Exception e) {
					System.err.print("Problem while loading user initializer class "+s0.substring(11));
					e.printStackTrace();
					return;
				}
				continue;
			}


			// we want to test these modules
			if (s0.startsWith("-tracesOutputFile=")) {
				tracesOutputFile=s0.substring(18);
				continue;
			}
			// we want to test these modules
			if (s0.startsWith("-tracesInputFiles=")) {
				String s1=s0.substring(18);
				traceInputFiles = s1.split(":");
				continue;
			}

			// If we want to gather the number of calls per method
			if (s0.equals("-printNumberOfCallsPerMethod")) {
				printNumberOfCallsPerMethod = true;
				continue;	
			}

			// If we want to approximate the number of bugs over number of calls
			if (s0.equals("-approximate")) {
				approximate = true;
				showMonitoringGui=true;
				continue;	
			}

			// we want to store the compact report in some file in a new line
			if (s0.startsWith("-compactReport=")) {
				Yeti.compactReport = true;
				reportFile=s0.substring(15);
				continue;
			}

			if (s0.startsWith("-outputUnitTestFile=")){
				saveInUnitTestFile=true;
				unitTestFileName=s0.substring(20);
				YetiLog.printDebugLog("Will save unit tests in: "+unitTestFileName, Yeti.class, true);
				continue;
			}
			
			// Dhana for plugin
			if (s0.equals("-plugin")) {
				isPlugin = true;
				continue;	
			}
			
			System.out.println("Yeti could not understand option: "+s0);
			Yeti.printHelp();
			return;

		}

		// we read traces if there are some to read and we initialize the list of Errors with it.
		HashMap<String, Object> initialListOfErrors = null;
		if (traceInputFiles!=null) {
			initialListOfErrors = new HashMap<String, Object>();
			// for each file to use, we read the traces and add them to our initial list
			for (String fileName: traceInputFiles) {
				for (String trace: YetiLog.proc.readTracesFromFile(fileName)) {
					initialListOfErrors.put(trace, 0);
				}
			}

		}

		//TODO: Init with chromosome interpreter
		//test of options to set up the YetiProperties for Java
		if (isJava) {
			YetiLoader prefetchingLoader = new YetiJavaPrefetchingLoader(yetiPath);
			YetiInitializer initializer = new YetiJavaInitializer(prefetchingLoader);
			YetiTestManager testManager = new YetiJavaTestManager();
			logProcessor = new YetiJavaLogProcessor(initialListOfErrors);
			pl=new YetiJavaProperties(initializer, testManager, logProcessor);			
		}

		//test of options to set up the YetiProperties for JML
		if (isJML) {
			YetiLoader prefetchingLoader = new YetiJMLPrefetchingLoader(yetiPath);
			YetiInitializer initializer = new YetiJavaInitializer(prefetchingLoader);
			YetiTestManager testManager = new YetiJavaTestManager();
			logProcessor = new YetiJavaLogProcessor(initialListOfErrors);
			pl=new YetiJavaProperties(initializer, testManager, logProcessor);
		}


		//test of options to set up the YetiProperties for CoFoJa
		if (isCoFoJa) {
			YetiLoader prefetchingLoader = new YetiCoFoJaPrefetchingLoader(yetiPath);
			YetiInitializer initializer = new YetiJavaInitializer(prefetchingLoader);
			YetiTestManager testManager = new YetiJavaTestManager();
			logProcessor = new YetiJavaLogProcessor(initialListOfErrors);
			pl=new YetiJavaProperties(initializer, testManager, logProcessor);
		}

		//test of options to set up the YetiProperties for .NET assemblies
		if (isDotNet) {

			System.out.println("****************************************");
			System.out.println("STARTING CsharpReflexiveLayer.exe ");
			System.out.println("****************************************");

			th = new Thread(new Runnable()
			{

				public void run() {
					Runtime run = Runtime.getRuntime();
					String command = yetiPath + "CsharpReflexiveLayer.exe";					
					try {
						Process p = run.exec(command);						
						InputStream in = p.getInputStream();						

						@SuppressWarnings("unused")
						int c;
						while ((c = in.read()) != -1) {
							//System.out.print((char) c);
						}

					} catch (IOException e) {					
						YetiCsharpInitializer.initflag=true;
					}
				}
			} );



			th.start();


			YetiInitializer initializer = new YetiCsharpInitializer();
			YetiTestManager testManager = new YetiCsharpTestManager();
			logProcessor = new YetiCsharpLogProcessor(initialListOfErrors);
			YetiServerSocket socketConnector = new YetiServerSocket();
			pl=new YetiCsharpProperties(initializer, testManager, logProcessor, socketConnector);
			System.out.println("\nMaking the .NET test-case calls...\n");			
			System.out.println("----------------------------------------");

		}
		
		
		

		//test of options to set up the YetiProperties for Java
		if (isCommandLine) {
			YetiInitializer initializer = new YetiCLInitializer();
			YetiTestManager testManager = new YetiCLTestManager();
			logProcessor = new YetiCLLogProcessor();
			pl=new YetiCLProperties(initializer, testManager, logProcessor);			
		}

		//test of options to set up the YetiProperties for Java
		if (isPharo) {
			YetiPharoCommunicator initializer = new YetiPharoCommunicator();
			try {
				initializer.initialize(args);
			} catch (YetiInitializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			YetiTestManager testManager = new YetiPharoTestManager(initializer);
			logProcessor = null; //new YetiJavaLogProcessor(initialListOfErrors);
			pl=new YetiJavaProperties(initializer, testManager, logProcessor);
		}


		//if it is raw logs, then set it		
		if (isRawLog) {
			pl.setRawLog(isRawLog);
		}

		//if it is no logs, then set it		
		if (isNoLogs) {
			pl.setNoLogs(isNoLogs);
		}

		if (isEvolutionary) {
			System.out.println("--==> Evolving a testing Strategy");
			YetiStrategyOptimizer optimizer = new YetiStrategyOptimizer(gaParameters);
			optimizer.evolveStrategy();
		}

		//TODO: IF GA init with chromosome
		// initializing Yeti
		try {
			pl.getInitializer().initialize(args);
		} catch (YetiInitializationException e) {
			//should never happen
			e.printStackTrace();
		}

		// calls the secondary initializer
		if (secondaryInitializer!=null) {
			try {
				secondaryInitializer.initialize(args);
			} catch (YetiInitializationException e1) {
				// if there is an issue with the custom initialization
				System.err.print("Problem while executing user initializer class "+secondaryInitializer.getClass().getName());
				e1.printStackTrace();
				return;

			}
		}

		// create a YetiTestManager and 
		YetiTestManager testManager = pl.getTestManager(); 

		//sets the calls timeout
		if (!(callsTimeOut<=0)) {
			testManager.setTimeoutInMilliseconds(callsTimeOut);
		}

		// We set the strategy
		strategy= new YetiRandomStrategy(testManager);
		if (isRandomPlus) {
			strategy= new YetiRandomPlusStrategy(testManager);				
		}
		if (isRandomPlusPeriodic) {
			strategy= new YetiRandomPlusPeriodicProbabilitiesStrategy(testManager);
		}			
		if (isRandomPlusDecreasing) {
			strategy= new YetiRandomPlusDecreasing(testManager);
		}		
		if (isDSSR) {
			strategy= new YetiDSSRStrategy(testManager);
		}

		if (isRunningFromChromosome) {
			if (chromosome == null) {
				YetiStrategyOptimizer optimizer = new YetiStrategyOptimizer(gaParameters);
				try {
					optimizer.createGAConfiguration();
					IChromosome loadedChromosome = YetiStrategyPersistenceManager.loadChromosome(optimizer.gaConfiguration, chromosomePath);
					YetiChromosomeInterpreter chromosomeInterpreter = new YetiChromosomeInterpreter(loadedChromosome);
					strategy = new YetiEvolutionaryStrategy(testManager,chromosomeInterpreter);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				YetiChromosomeInterpreter chromosomeInterpreter = new YetiChromosomeInterpreter(chromosome);
				strategy = new YetiEvolutionaryStrategy(testManager,chromosomeInterpreter);
			}
		}

		//TODO: lssilva make sure nothing that is not for EV is executed after the ev
		// is started
		if (isEvolutionary) {
			//Take the chromosome set by GA

			if (strategy == null) {
				//    strategy = new YetiEvolutionaryStrategy(testManager, chromosome);
			}
		}

		// getting the module(s) to test
		YetiModule mod=null;

		//we generate an array which will contain all the modules to be tested
		ArrayList<YetiModule> modules=new ArrayList<YetiModule>();

		//we iterate through the list of testModules specified by user
		for(String moduleToTest : modulesToTest) {

			//we check if sub-packages are to be included (test modules ending with asterisks specifies yes)
			if (moduleToTest.endsWith("*"))
			{
				boolean ignoreCriteria = false;
				if (moduleToTest.equals("*")) ignoreCriteria = true; //include all the classes, ignore all criteria

				//parse the name of parent package
				String parentPackage = moduleToTest.replace(".*", "");

				//iterate through all the loaded classes from classpaths
				Iterator it = YetiModule.allModules.keySet().iterator();
				while(it.hasNext()) 
				{	
					String moduleName = (String)it.next();

					//if classname is same as the parent package or criteria is to be ignored, 
					//add it in the array of test modules
					if (moduleName.startsWith(parentPackage) || ignoreCriteria)
					{
						addTestModuleInArray(moduleName, modules);
					}
				}
			}
			else // a single test module with no sub-packages to be included (no asteriks)
			{
				// add the module in the collection array of test modules
				addTestModuleInArray(moduleToTest, modules);
			}

		}
		//in case no test modules were successfully loaded
		if (modules.size() == 0)
		{
			System.err.println("Testing halted: No test modules were successfully loaded");
			printHelp();
			return;

		}


		//we combine all the modules in single structure
		mod = YetiModule.combineModules(modules.toArray(new YetiModule[modules.size()]));

		//in case no test modules were successfully loaded
		if (mod.routinesInModule.size() == 0)
		{
			System.err.println("Testing halted: there is no method to test");
			printHelp();
			return;

		}

		// we let everybody use the tested module
		Yeti.testModule = mod;

		// creating the engine object
		engine= new YetiEngine(strategy,testManager);

		// Creating the log processor
		if (showMonitoringGui) {
			YetiLog.proc=pl.getLogProcessor();

			//The routine tracker
			gui = new YetiGUI(100);


		} else {
			YetiLog.proc=pl.getLogProcessor();
		}

		// logging purposes:
		long startTestingTime = new Date().getTime();
		st=startTestingTime;
		// depending of the options launch the testing
		if (isNTests)
			// if it is the number of states
			engine.testModuleForNumberOfTests(mod, nTests);
		else if (isTimeout) 
			// if it is according to a timeout
			engine.testModuleForNSeconds(mod, timeOutSec);
		else {
			System.out.println("Specify a time or a number of tests to perform");
			printHelp();
			return;
		}
		// logging purposes:
		long endTestingTime = new Date().getTime();

		// for logging purposes
		if (isTimeout) {

			System.out.println("/** Testing Session finished, time: "+(endTestingTime-startTestingTime)+"ms **/");
		}


		boolean isProcessed = false;
		String aggregationProcessing = "";
		// presents the logs
		System.out.println("/** Testing Session finished, number of tests:"+YetiLog.numberOfCalls+", time: "+(endTestingTime-startTestingTime)+"ms , number of failures: "+YetiLog.numberOfErrors+"**/");	
		if (!Yeti.pl.isRawLog()&&!Yeti.pl.isNoLogs()) {
			isProcessed = true;
			
			// we first process logs
			processedTestCases = YetiLog.proc.processLogs();
			if (saveInUnitTestFile){
				
				// we generate the file and its name
				String fileContent = YetiLog.proc.generateUnitTestFile(processedTestCases, unitTestFileName);
				String fileName=YetiLog.proc.generateUnitTestFileName(processedTestCases, unitTestFileName);
				
				// we write it
				try {
					PrintStream ps = new PrintStream(fileName);
					ps.print(fileContent);
				} catch (Exception e) {
					// in case it did not work
					System.out.println("/** Problem with saving test cases. Printing them on command-line instead. **/");
					e.printStackTrace();
					System.out.println(fileContent);
				}


			} else {
				// otherwise, we put it on the command-line
				System.out.println(YetiLog.proc.generateUnitTestFile(processedTestCases, unitTestFileName));
			}

			// logging purposes: (slightly wrong because of printing)
			long endProcessingTime = new Date().getTime();
			aggregationProcessing = "/** Processing time: "+(endProcessingTime-endTestingTime)+"ms **/";
		}

		// we create the report
		report = new YetiReport(modulesString, YetiLog.numberOfCalls , endTestingTime-startTestingTime, YetiLog.numberOfErrors);

		if (!isProcessed) {

			YetiLogProcessor lp = (YetiLogProcessor)Yeti.pl.getLogProcessor();
			System.out.println("/** Unique relevant bugs: "+lp.getNumberOfUniqueFaults()+" **/");
			report.setnErrors(lp.getNumberOfUniqueFaults());

		}
		if (isProcessed) {
			System.out.println("/** Testing Session finished, number of tests:"+YetiLog.numberOfCalls+", time: "+(endTestingTime-startTestingTime)+"ms , number of failures: "+YetiLog.numberOfErrors+"**/");
			System.out.println(aggregationProcessing);

		}

		if (Yeti.hasBranchCoverage) {
			try {
				System.out.println("/** " +
						Yeti.testModule.getCoverageKind()+": "+
						Yeti.testModule.getNumberOfCoveredBranches()+"/"+
						Yeti.testModule.getNumberOfBranches()+"("+
						((float)((int)(100*Yeti.testModule.getCoverage())))/100+"%) **/");
				report.setBranchCoverage(Yeti.testModule.getCoverage());
			} catch (YetiNoCoverageException e) {
				// this should not happen... We should have quit already
				e.printStackTrace();
			}
		}
		// if users want to print the traces in an outputFile, we do it now
		if ((tracesOutputFile!=null)&&(logProcessor!=null)) {
			YetiLog.proc.outputTracesToFile(logProcessor.listOfNewErrors, tracesOutputFile,logProcessor.numberOfNonErrors);
		}

		// if we print the number of calls per method
		if (printNumberOfCallsPerMethod) {
			System.out.println("Trace of number of calls per method:");
			for (YetiRoutine r: Yeti.testModule.routinesInModule.values()) {
				System.out.println(r.getSignature()+": Called: "+r.getnTimesCalled()+", Successfully: "+r.getnTimesCalledSuccessfully()+", Undecidable: "+r.getnTimesCalledUndecidable()+", Unsuccessfully: "+r.getnTimesCalledUnsuccessfully());
			}
		}

		// if we approximate the number of failures per number of testsZ
		if (approximate) {
			YetiDataSet dataSetNcallsNFaults = new YetiDataSet(YetiGUI.mainGUI.getGraphNumberOfCallsOverTime().series[1], YetiGUI.mainGUI.getGraphFaults().series[1]);
			YetiMichaelisMentenEquation e = dataSetNcallsNFaults.fitMichaelisMenten();
			report.setK(e.getK());
			report.setMax(e.getMax());
			report.setR2(dataSetNcallsNFaults.coeffOfDetermination(e));
			report.setSSErr(dataSetNcallsNFaults.SSerr);
			report.setSSTot(dataSetNcallsNFaults.SStot);

			System.out.println("/** Approximation: "+e+"(f: number of bugs, x number of tests) R^2="+dataSetNcallsNFaults.coeffOfDetermination(e)+" **/");
		}

		//If distributed mode of operation
		if (isDistributed){
			YetiMap.moduleName=modulesToTest[0];
			YetiLogProcessor lp = (YetiLogProcessor)Yeti.pl.getLogProcessor();
			YetiMap.listOfExceptions.putAll(lp.getListOfErrors());
		}

		// if we ask for a report file, we complete it
		if(reportFile!=null) {
			try {
				FileOutputStream fos = new FileOutputStream(reportFile, true);
				fos.write((report.toString()+"\n").getBytes());
				fos.close();
				Yeti.reset();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("could not print in "+reportFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isPlugin)
		{
			
			YetiPlugIn.getSummaryInfoArrayList().addSummary("/** Testing Session finished, time: "+(endTestingTime-startTestingTime)+"ms **/");
			YetiPlugIn.getSummaryInfoArrayList().addSummary("/** Testing Session finished, number of tests:"+YetiLog.numberOfCalls+", time: "+(endTestingTime-startTestingTime)+"ms , number of failures: "+YetiLog.numberOfErrors+"**/");
			YetiPlugIn.getSummaryInfoArrayList().addSummary("/** Testing Session finished, number of tests:"+YetiLog.numberOfCalls+", time: "+(endTestingTime-startTestingTime)+"ms , number of failures: "+YetiLog.numberOfErrors+"**/");
			YetiPlugIn.getSummaryInfoArrayList().addSummary(aggregationProcessing);
			
			YetiPlugIn.getSummaryInfoArrayList().addSummary("/** Testing Session finished, number of tests:"+YetiLog.numberOfCalls+", time: "+(endTestingTime-startTestingTime)+"ms , number of failures: "+YetiLog.numberOfErrors+"**/");
			if ( YetiLog.proc.listOfNewErrors.size()>0)
				Marker.insertMarker(YetiLog.proc.listOfNewErrors);
						
			processedTestCases = YetiLog.proc.processLogs();
			//	YetiPlugIn.getSummaryInfoArrayList().addSummary("Unit Test Cases");
			//	YetiPlugIn.getSummaryInfoArrayList().addSummary(fileContent);
			System.out.println("Dhana test case begins");
			//	System.out.println(fileContent);
				
				int lineno=0;
				for (String tc: processedTestCases) {	
					lineno= lineno+1 ;
					YetiPlugIn.getOverviewArrayList().addOverview( lineno, tc, "");
				}
						
		}


		System.out.println("/** Testing finished **/");


	}

	/**
	 * Adds a single Yeti Module to the modules array
	 * @param moduleToTest the name of the module to be added
	 * @param modules the array in which the module is to be added
	 *
	 */
	private static void addTestModuleInArray(String moduleToTest, ArrayList<YetiModule> modules)
	{
		YetiModule yetiModuleToTest = YetiModule.allModules.get(moduleToTest);
		if(yetiModuleToTest==null) {
			System.err.println(moduleToTest + " was not found. Please check");
			System.err.println(moduleToTest + " is skipped from testing");
		} else {
			modules.add(yetiModuleToTest);
		}
	}

	/**
	 * This method completely resets YETI.
	 */
	public static void reset() {
		try {
			System.out.println("Resetting");
			Yeti.engine = null;
			Yeti.hasBranchCoverage = false;
			Yeti.isDistributed = false;
			Yeti.makeMethodsVisible = false;
			Yeti.st = 0;
			Yeti.strategy.reset();
			Yeti.strategy = null;
			Yeti.testModule = null;
			Yeti.testModulesName = null;
			Yeti.yetiPath = ".";
			if (Yeti.gui!=null)
				Yeti.gui.reset();
			YetiType.reset();
			YetiVariable.reset();
			YetiLoader.reset();
			YetiIdentifier.reset();
			YetiLog.reset();
			YetiLogProcessor.reset();
			YetiModule.reset();
			YetiName.reset();
			System.out.println("Resetting");
			if (Yeti.pl instanceof YetiJavaProperties) {
				YetiJavaMethod.reset();
				YetiJavaTestManager.reset();
				System.out.println("2");
			}
			System.out.println("Reset Properties");
			// TODO add the same code for other programming languages
			Yeti.pl = null;
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("Error in reset");
		}
	}


	/**
	 * This is a simple help printing utility function.
	 */
	public static void printHelp() {
		System.out.println("Yeti Usage:\n java yeti.Yeti [-java|-Java] [[-time=Xs|-time=Xmn]|[-nTests=X]][-testModules=M1:M2:...:Mn][-help|-h][-rawlogs]");
		System.out.println("\t-help, -h: prints the help out.");
		System.out.println("\t-java, -Java : for calling it on Java.");
		System.out.println("\t-jml, -JML : for calling it on JML annotated code.");
		System.out.println("\t-cofoja, -CoFoJa : for calling it on Java programs annotated with CoFoJa.  Note that if you do not want to pre-process the classes statically, you should call Yeti using the option -javaagent:cofoja.jar (or any other path to a CoFoJa jar).");
		System.out.println("\t-dotnet, -DOTNET : for calling it on .NET assemblies developed with Code-Contracts.");
		System.out.println("\t-time=Xs, -time=Xmn : for calling Yeti for a given amount of time (X can be minutes or seconds, e.g. 2mn or 3s ).");
		System.out.println("\t-nTests=X : for calling Yeti to attempt X method calls.");
		System.out.println("\t-testModules=M1:M2:...:Mn : for testing one or several modules. Sub-packages of a system can also be specified with asteriks e.g. yeti.test.* will include all the classes in yeti.test + all the classes belonging to the sub-packages of yeti.test .");
		System.out.println("\t-initClass=X : this will use a user class to initialize the system this class will be a subclass of yeti.environments.YetiInitializer .");
		System.out.println("\t-outputUnitTestFile=X : this option stores the generated test cases in a file. THis is binding-specific for Java: X=tests/test0.T.java will store a file T.java from package test0 into the directory tests.");
		System.out.println("\t-rawlogs: prints the logs directly instead of processing them at the end.");
		System.out.println("\t-nologs : does not print logs, only the final result.");
		System.out.println("\t-msCalltimeout=X : sets the timeout (in milliseconds) for a method call to X.Note that too low values may result in blocking Yeti (use at least 30ms for good performances).");
		System.out.println("\t-yetiPath=X : stores the path that contains the code to test (e.g. for Java the classpath to consider)");
		System.out.println("\t-newInstanceInjectionProbability=X : probability to inject new instances at each call (if relevant). Value between 0 and 100, default is 25.");
		System.out.println("\t-probabilityToUseNullValue=X : probability to use a null instance at each variable (if relevant). Value between 0 and 100, default is 1.");
		System.out.println("\t-randomPlus : uses the random+ strategy that injects interesting values every now and then.");
		System.out.println("\t-randomPlusPeriodic : uses the random+ strategy and periodically change the values of the standard probalilities (null values, new instances, interesting values).");
		System.out.println("\t-randomPlusDecreasing : uses the random+ strategy and decreases the values of the standard probalilities (null values, new instances, interesting values).<br>");
		System.out.println("\t-evolutionary : uses GA to evolve a testing strategy.");
		System.out.println("\t-chromosome : execute Yeti using a strategy chromosome.");
		System.out.println("\t-DSSR : initially uses random+ strategy and based on the results of random+ it uses Dirt Spot Sweeping Random strategy.");
		System.out.println("\t-gui : shows the standard graphical user interface for monitoring yeti.");
		System.out.println("\t-noInstancesCap : removes the cap on the maximum of instances for a given type. Default is there is and the max is 1000.");
		System.out.println("\t-instancesCap=X : sets the cap on the number of instances for any given type. Defaults is 1000.");
		System.out.println("\t-tracesOutputFile=X : the file where to output traces on disk.");
		System.out.println("\t-tracesInputFiles=X : the files where to input traces from disk (file names separated by ':').");
		System.out.println("\t-printNumberOfCallsPerMethod : prints the number of calls per method.");
		System.out.println("\t-branchCoverage : shows the branch coverage if available (in Java, this implies instrumenting the bytecode).");
		System.out.println("\t-makeMethodsVisible: converts all the protected and private methods into public for testing.");
		System.out.println("\t-approximate : approximates the number of unique failures per number of tests.");
		System.out.println("\t-compactReport=X : adds a line in file X containing the information about the testing session.");

	}

}