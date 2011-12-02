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
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Control;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

public class OverviewContentProvider implements IStructuredContentProvider
			,IPropertyChangeListener
{
	
	private StructuredViewer viewer;

	 
	 public OverviewContentProvider()
	 {
			
	 }
	 	 
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		
		
		
		if (viewer == null)
		      this.viewer = (StructuredViewer) v;
		
		
		
		    if (oldInput != newInput) { // if not the same

		      if (newInput != null) { // add listener to new - fires even if old is null
		        ((IOverviewArrayList) newInput).addPropertyChangeListener(this);
		      }

		      if (oldInput != null) { // remove from old - fires even if new is null
		        ((IOverviewArrayList) oldInput).removePropertyChangeListener(this);
		      }
		    }
		  
	}
	public void dispose() {
		 viewer = null;
	}
	
	@Override
	public Object[] getElements(Object parent) {
		// TODO Auto-generated method stub
		
		return ((IOverviewArrayList) parent).getOverview();
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		
		  Control ctrl = viewer.getControl();
	    if (ctrl != null && !ctrl.isDisposed()) {

	      ctrl.getDisplay().asyncExec(new Runnable() {

	        public void run() {
	          if (event.getProperty() == IOverviewArrayList.LISTREFRESH )
	            viewer.refresh();

	          else {
	            String[] propChange = new String[] {event.getProperty()};
	            viewer.update(event.getNewValue(), propChange);
	          }
	        }
	      });
	    }
		
	}
	
	
	
}


