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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;


import yeti.Yeti;
import yetiplugin.Util;
import yetiplugin.YetiPlugIn;
import yetiplugin.marker.Marker;

//import org.apache.log4j.Logger;

public class CheckCodeAction implements IObjectActionDelegate{

	public static IWorkbenchPage workbenchPage;	
	private IWorkbenchWindow window;
	private ISelection selection = null;
	/**
	 * Constructor for Action1.
	 */
	public CheckCodeAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		workbenchPage = targetPart.getSite().getPage();
		this.window = workbenchPage.getWorkbenchWindow();
	}
	
	public void run(IAction action) 
	{ 
		String testClassName = "";
		String selResourceName ="";
		String selPackageName ="";
		String selTestModule  ="";
		String selFileClasspath="";
		Job yetiTestingJob;
		String preferenceString ;
		IFile selectedTestModuleFile ;
		boolean isProcessed = false ;
		boolean isError = false;
		IJavaProject projectName;
		 
		
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		IResource resource = null;        
	    Object[] selectedFileObjectArray = structuredSelection.toArray();
        for (Object selectedObject : selectedFileObjectArray)
        {         
           if (selectedObject instanceof IResource) 
            {      		
        	     isProcessed = true; 
	             resource = (IResource) selectedObject;
	             selResourceName =resource.getName();
	             if (resource instanceof IFile)
	             {     	
	            	 selectedTestModuleFile = (IFile) resource;            	
	            	 IJavaElement element = JavaCore.create(selectedTestModuleFile);
	            	 
	            	 if (element instanceof ICompilationUnit) {
	            		 ICompilationUnit selectedClassName = (ICompilationUnit) element;
	            		 try {
	            			  // Check for class file have any error  
	            			 if ( selectedClassName.isStructureKnown()== true )
	            			 {  
		            			 IPackageDeclaration[] packagename = selectedClassName.getPackageDeclarations();
		            			 for (IPackageDeclaration pack : packagename)
		            		        {         
		            				 selPackageName = pack.getElementName();
		            			 
		            		        }
		            			 projectName = selectedClassName.getJavaProject();	            			                			
	            			     selFileClasspath = YetiPlugIn.relativeToAbsolute(projectName.getOutputLocation()).toString();
	            			     
	            			 }   
	            			 else
	            			 {
	            				 isError = true;
	            			 }
	            				 
		            		 	} catch (JavaModelException e) 
		            		 	{
								e.printStackTrace();
		            		 	}
	            	 		}
	             }	 
	             
          }
           
        }   	      
        
        if (isError ==false)
        {	
			 Marker.setResource(resource);
			 // Deleting  Markers
			 Marker.deleteMarker(resource);
			 //  Generating Test Module Name.   	      
			 testClassName =  selResourceName.substring(0, selResourceName.length()-5);  
			 if ( selPackageName.equals("") )
				 selTestModule = testClassName ;
			 else 
				 selTestModule = selPackageName+"." + testClassName;
			 
			 YetiPlugIn.realtimeGUI = true;
			 
			//	Calling Yeti Engine 
			 preferenceString = Util.getYetiPreferenceValue(selTestModule ,selFileClasspath).toString();
			 final String[] yetiArgumentStringArray = preferenceString.split(" ");
	 
		   	 
		     	yetiTestingJob = new Job("Checking Yeti..")
		     	{
		    	 	protected IStatus run(IProgressMonitor monitor) 
					{
						Yeti.main(yetiArgumentStringArray);
						Yeti.reset();  
					    return Status.OK_STATUS;
					}
				 };
				 yetiTestingJob.setPriority(Job.SHORT);
				 yetiTestingJob.schedule();
         }	
        else
        {
        	MessageDialog.openInformation(
    				window.getShell(),
    				"Yeti",
    				"Please correct error on the file before running YETI test.");
        }
        
         if ( isProcessed == false) 
         {
        	 MessageDialog.openInformation(
        				window.getShell(),
        				"Yeti",
        				"Please select the file in Project Explorer");
         }
         yetiTestingJob = null;
         
	}
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		 this.selection = selection;
	}

}
