
package yetiplugin.popup.actions;

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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import yetiplugin.YetiPlugIn;
import yetiplugin.marker.Marker;

public class ClearMarkerAction implements IObjectActionDelegate {

	private ISelection selection = null;
	@Override
	public void run(IAction action) 
	{
		IStructuredSelection sel = (IStructuredSelection) selection; 
        IResource resource = null;
        
        Object[] objects = sel.toArray();
        for (Object selObject : objects)
        {
        	if (selObject instanceof IResource) 
            {
                resource = (IResource) selObject;
                Marker.deleteMarker(resource);
            }    
          
        }
        	clearAllView ();
	  }
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {		
		 this.selection = selection;
	}
	
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {		
	}
	
	public static void clearAllView()
	{
		// Clear Summary View
		YetiPlugIn.getSummaryInfoArrayList().resetList();
		// Clear Over
		YetiPlugIn.getOverviewArrayList().resetList();
		// Clear GUI
		
	}
}

