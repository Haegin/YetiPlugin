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
import java.util.ArrayList;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.PropertyChangeEvent;

@SuppressWarnings({ "deprecation", "unchecked", "serial" })

public class OverviewArrayList extends ArrayList implements IOverviewArrayList
{
	  private transient ListenerList propertyChangeListeners = null;
	  
 //	  private static IOverviewArrayList varOverview =  new OverviewArrayList();
 	  
 	public OverviewArrayList() {
 		    super();
 		    resetList();
 		   
 		  }
 /*	public static IOverviewArrayList getInstance() {
 			return varOverview;
 		}
 */		
	@Override
	public void resetList() {
		clear();
		firePropertyChange(LISTREFRESH, null, null);
	}
	@Override
	public Object[] getOverview() {
		return toArray();
	}
	@Override
	public IOverview addOverview(Integer testCaseId,String testCase, 
			 String testModule) {
		
		IOverview tempVar = new Overview(testCase,testCaseId,testModule);
		add(tempVar);
		
		firePropertyChange(LISTREFRESH, null, tempVar);
		return tempVar;
	}
	@Override
	public void removeOverview(IOverview OverviewElement) {
		remove(OverviewElement);
		firePropertyChange(LISTREFRESH,  OverviewElement,null);
		
	}
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		getPropertyChangeListeners().remove(listener);
	}
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		getPropertyChangeListeners().add(listener);
	}
	
	void firePropertyChange(String changeId, Object oldValue,
		      Object newValue) 
	 {
			
		    final PropertyChangeEvent event = new PropertyChangeEvent(this,
		        changeId, oldValue, newValue);

		    Object[] listeners = getPropertyChangeListeners().getListeners();
		    for (int i = 0; i < listeners.length; i++) {
		      ((IPropertyChangeListener) listeners[i]).propertyChange(event);
		    }
	 }
	private ListenerList getPropertyChangeListeners() 
	{
	  if (propertyChangeListeners == null)
	      propertyChangeListeners = new ListenerList();
	    return propertyChangeListeners;
	 }
}
