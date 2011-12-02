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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;

import yetiplugin.YetiPlugIn;


public class ViewTestCase extends ViewPart implements  ISelectionChangedListener
{
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "yetiplugin.views.YetiView";
	private TableViewer viewer;	
	/**
	 * The constructor.
	 */
	public ViewTestCase() {
	}
	
	class NameSorter extends ViewerSorter {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		Table table= createTableWithColumns(parent);
		  viewer = new TableViewer(table);
		  	
		  GridData gd = new GridData(GridData.FILL_BOTH);
		  viewer.getControl().setLayoutData(gd);
		    
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "YetiPlugIn.viewer");
		
		
		viewer.setContentProvider(new OverviewContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		viewer.setLabelProvider(new OverviewLabelProvider());
		viewer.setInput(YetiPlugIn.getOverviewArrayList());
		
		/* 
		
		 viewer.setContentProvider( new ViewSummaryContentProvider());
			// Get the content for the viewer, setInput will call getElements in the
			// contentProvider
		 viewer.setLabelProvider(new ViewSummaryLabelProvider());
		    viewer.setInput(YetiPlugIn.getSummaryInfoArrayList());
		    
		// Share Viewer Selection with other workbench parts
	    getViewSite().setSelectionProvider(viewer);
		viewer.addSelectionChangedListener(this);
		*/
	}
	
	 private Table createTableWithColumns(Composite parent) {
		    Table table = new Table(parent, SWT.H_SCROLL | SWT.V_SCROLL
		        | SWT.MULTI | SWT.FULL_SELECTION);

		    TableLayout layout = new TableLayout();
		    table.setLayout(layout);

		    table.setLinesVisible(true);
		    table.setHeaderVisible(true);
		    String[] STD_HEADINGS = {
		        "TestCase Id ", "Test Case Description", "Test Module"};

		    layout.addColumnData(new ColumnWeightData(10, 80, true));
		    TableColumn tc0 = new TableColumn(table, SWT.NONE);
		    tc0.setText(STD_HEADINGS[0]);
		    tc0.setAlignment(SWT.LEFT);
		    tc0.setResizable(true);

		    layout.addColumnData(new ColumnWeightData(20, 800, true));
		    TableColumn tc1 = new TableColumn(table, SWT.NONE);
		    tc1.setText(STD_HEADINGS[1]);
		    tc1.setAlignment(SWT.LEFT);
		    tc1.setResizable(true);
		    
		    layout.addColumnData(new ColumnWeightData(30, 400, true));
		    TableColumn tc2 = new TableColumn(table, SWT.NONE);
		    tc2.setText(STD_HEADINGS[2]);
		    tc2.setAlignment(SWT.LEFT);
		    tc2.setResizable(true);
		    		    
		    return table;
		  }


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	 public void dispose() {
		    // Clean up listener when view is closed
		    viewer.removeSelectionChangedListener(this);
		   
		    
		  }

}

