package yetiplugin.marker;

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

import java.util.HashMap;
import java.util.Iterator;


import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import yetiplugin.YetiPlugIn;

public class Marker {
	
	public static final String NAME = "yeti.YetiMarker";
	private static final IMarker[] EMPTY = new IMarker[0];
	private static IResource resource;

	public static IResource getResource() {
		return resource;
	}
	public static void setResource(IResource resource) {
		Marker.resource = resource;
	}
	
	public static IMarker[] getAllMarkers(IResource fileOrFolder)
	{
		return getMarkers(fileOrFolder, IResource.DEPTH_INFINITE);
	}
	public static IMarker[] getMarkers(IResource fileOrFolder, int depth){
		try 
		{			
			return fileOrFolder.findMarkers(Marker.NAME, true, depth); 
		} 	catch (CoreException e) 
		{	
		}
		return EMPTY;
		}	
	public static  void deleteMarker(IResource resource)
	{
		// Clear summary and overview Details
		clearAllViews();
		
		
		// Clear Markers 
			IMarker[] allMarkers = getAllMarkers(resource);
		
			for (IMarker marker : allMarkers) {
				try {
					marker.delete();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
	
	public static void clearAllViews()
	{   
		try 
		{
			// clear Summary Details
			if ( YetiPlugIn.summaryList.getSummaryInfo().length > 0 )
			{
				YetiPlugIn.summaryList.resetList();
			}
			// Clear Overview details
			if (YetiPlugIn.overview.getOverview().length >0)
			{	
				YetiPlugIn.overview.resetList();
			}	
		
		}
		catch ( Exception e)
		{
			System.out.println("Exception error"+ e.getMessage());
		}
	}
	
	public static  void insertMarker(HashMap<String,Object> listOfErrors)
	{
 	      IMarker m;
 	      int lineNumber; 
 	      String errorMessage;
	      
			Iterator<String> traces = listOfErrors.keySet().iterator();
			
			for (int i=0; i<listOfErrors.size();i++) {
				try {			
			
				   m = resource.createMarker(Marker.NAME);
				   errorMessage = traces.next();
				   lineNumber = findLineNo(errorMessage);
		    	   m.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		    	   m.setAttribute(IMarker.MESSAGE, errorMessage);
		    	   m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
		    	   
		    	   // TODO Marker severity should be changed based on preference settings.
		    	   m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		    	   
		    	   // whether the marker will be saved as part of the workspace
		    	   m.setAttribute(IMarker.TRANSIENT, false);
				
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	public static int findLineNo(String errorMessage)
	{  
		int lineNumber=0;
		
//java.lang.ArithmeticException: / by zero at Eta.divideInteger(Eta.java:7)
	//	errorMessage.lastIndexOf(ch)
		
		try 
		{
		System.out.println("errorMessage:"+errorMessage);
	
		int colon         = errorMessage.lastIndexOf(":");
		int endBracket    = errorMessage.lastIndexOf(")");
		
		if ( colon > 0 &&  endBracket > 0 )
			lineNumber =	Integer.parseInt(errorMessage.substring(colon+1, endBracket));
		
		System.out.println("lineNumber:"+lineNumber);
		}
		catch (Exception  e)
		{
			return 0;
		}
		return lineNumber;
	}
}
