package yetiplugin.views;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import yeti.Yeti;
import yeti.YetiLog;
import yeti.YetiModule;
import yeti.YetiRoutine;
import yeti.monitoring.YetiGraph;
import yeti.monitoring.YetiGraphFaultsOverTime;
import yeti.monitoring.YetiGraphNumberOfCallsOverTime;
import yeti.monitoring.YetiGraphNumberOfFailuresOverTime;
import yeti.monitoring.YetiGraphNumberOfVariablesOverTime;
import yeti.monitoring.YetiRoutineGraph;
import yeti.monitoring.YetiUpdatable;
import yetiplugin.YetiPlugIn;


public class ViewGraph extends ViewPart implements   ActionListener, Runnable{

	final ViewGraph thisView;

	public ArrayList<YetiGraph> graphs = new ArrayList<YetiGraph>(4);
	
	public static ArrayList<YetiUpdatable> allComponents= new ArrayList<YetiUpdatable>();


	//	public ArrayList<Double> []series = new ArrayList[2];

	private Composite graphComposite;
	private Composite graphCompositeParent;
	/**
	 * The constructor.
	 */
	public ViewGraph() {
		thisView = this;
	}
	/**
	 * Creates the user interface for a view that can host a mix of SWT and 
	 * Swing components.
	 */
	public void createPartControl(Composite parent) 
	{
		graphCompositeParent = parent;
		addGraphs();
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	
	private void addGraphs()
	{
		
			
			graphComposite = new Composite(graphCompositeParent, SWT.EMBEDDED);
			final java.awt.Frame mainFrame = SWT_AWT.new_Frame(graphComposite);
	
			YetiGraph yetiGraphFaultsOverTime = new YetiGraphFaultsOverTime(YetiLog.proc,1000);
			graphs.add(yetiGraphFaultsOverTime);
		
			YetiGraph yetiGraphNumberOfCallsOverTime = new YetiGraphNumberOfCallsOverTime(YetiLog.proc,1000);
			graphs.add(yetiGraphNumberOfCallsOverTime);
		
			YetiGraph yetiGraphNumberOfVariablesOverTime = new YetiGraphNumberOfVariablesOverTime(YetiLog.proc,1000);
			graphs.add(yetiGraphNumberOfVariablesOverTime);
		
			YetiGraph yetiGraphNumberOfFailuresOverTime = new YetiGraphNumberOfFailuresOverTime(YetiLog.proc,1000);
			graphs.add(yetiGraphNumberOfFailuresOverTime);
			
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(0,2));			
			// we add all the graphs
			p.add(yetiGraphFaultsOverTime);
			p.add(yetiGraphNumberOfCallsOverTime);
			p.add(yetiGraphNumberOfFailuresOverTime);
			p.add(yetiGraphNumberOfVariablesOverTime);
	
		//	p.add(addModuleGraph());
			mainFrame.add(p);
			
			
			Thread t = new Thread(this);
			t.start();
		

			
	}
	
	@SuppressWarnings("static-access")
	private JPanel addModuleGraph()
	{
		JPanel sp = new JPanel();
		sp.setLayout(new GridLayout(0,3));

		// we generate the size of the panel depending on the number of methods we have
		int numberOfMethods = Yeti.testModule.routinesInModule.values().size();
		sp.setPreferredSize(new Dimension(300,10*numberOfMethods));
		sp.setBackground(Color.white);

		
		// we first sort the routines
		ArrayList<YetiRoutine> l = new ArrayList<YetiRoutine>();

		// we check all considered modules in the system
		ArrayList<YetiModule> modules =new ArrayList<YetiModule>();
		// if we test several modules at the same time
		if (Yeti.testModule.getCombiningModules()==null) {
			modules.add(Yeti.testModule);
			// otherwise
		} else {
			for (YetiModule mod: Yeti.testModule.getCombiningModules()) {
				modules.add(mod);
			}
		}

		// we build the list by sorting out instances
		for (YetiRoutine r: Yeti.testModule.routinesInModule.values()) {
			// if the routine is not defined in the considered module
			if (!modules.contains(r.getOriginatingModule()))
				continue;

			// if the size of the list is null, we add the first element
			if (l.size()==0) {
				l.add(r);
			} else {
				// otherwise, we iterate through and add it where needed 
				String signature = r.getSignature();
				for (int i=0; i<l.size(); i++) {
					// if we have found a bigger one, we insert the routine in there
					if (l.get(i).getSignature().compareTo(signature)>=0) {
						l.add(i, r);
						break;
					} else {
						// if we reached the maximum size, we insert it afterwards
						if (i==l.size()-1) {
							l.add(i+1,r);
							break;
						}
					}
				}
			}
		}
		
		
		// we add all routines to the panel of routines.		
		for (YetiRoutine r: l) {
			YetiRoutineGraph yetiRoutineGraph = new YetiRoutineGraph(r);
			yetiRoutineGraph.setSize(50, 30);
			sp.add(yetiRoutineGraph);
			this.allComponents.add(yetiRoutineGraph);
		}
		return sp;
	}
	
	public void run(){
		while(!Yeti.finished){
			for (YetiGraph g: graphs){
				g.sample();
				g.updateValues();
			}
			
			for (YetiUpdatable u: allComponents) {
				u.updateValues();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ( Yeti.finished == true)
		{
			YetiPlugIn.realtimeGUI = false;
		}	
	}
	
	public void setFocus() {
	
	}
	
	public void actionPerformed(ActionEvent e) {  }

}