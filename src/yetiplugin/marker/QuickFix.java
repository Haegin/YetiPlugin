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

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IMarkerResolution;
import yetiplugin.YetiPlugIn;
import yetiplugin.views.Overview;


public class QuickFix implements IMarkerResolution {	
	String label;
	Integer saveOption;
    IResource newTestCaseIResource;  
    
    QuickFix(String label, Integer saveOption) {
       this.label = label;
    }
    
    public String getLabel() {
       return label;
    }
    
    public void run(IMarker marker) {
    	Object[]  varTestCaseArrayList;
    
    	String errorMessage="";
    	String autoTestCase="";
    	String selPackageName="";
    	String yetiPackageName="";
    	boolean isFound = false;
    	try {
			errorMessage = marker.getAttribute(IMarker.MESSAGE).toString();
			YetiPlugIn.getOverviewArrayList().getOverview();
			varTestCaseArrayList =YetiPlugIn.getOverviewArrayList().getOverview();
			for ( Object varTestCase:varTestCaseArrayList )
			{
				 if ( varTestCase instanceof  Overview)
				 {
					 autoTestCase = ((Overview) varTestCase).getTestCase();
					 if ( autoTestCase.indexOf(errorMessage) >0 )
					 {
						 isFound = true;
						 break;
					 }
				 }
			}
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		if ( isFound == true )
		{
			Object selectedObject= marker.getResource();
			if (selectedObject instanceof IResource) 
			{
				newTestCaseIResource = (IResource)selectedObject;
				IProject currentProject = marker.getResource().getProject();
				IJavaProject javaProject= JavaCore.create( newTestCaseIResource.getProject());
    	   
	    	   if (selectedObject instanceof ICompilationUnit) {
	      		 ICompilationUnit selectedClassName = (ICompilationUnit) selectedObject;
	      		 try {
	      			 IPackageDeclaration[] packagename = selectedClassName.getPackageDeclarations();
	      			 for (IPackageDeclaration pack : packagename)
	      		        {         
	      				 selPackageName = pack.getElementName();
	      				 
	      		        }
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
				}
    	  
	    	   IFolder folder= currentProject.getFolder("src");
	    	   if ( ! folder.exists() )
			try {
				folder.create(true, true, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	   
    	   IPackageFragmentRoot srcFolder= javaProject.getPackageFragmentRoot(folder);
    	   IPackageFragment fragment=null;
    	    	     
    	   if(  selPackageName.equals(""))
    	   {
    		   yetiPackageName = "test";
    	   }
    	   else
    	   {
    		   yetiPackageName  = selPackageName+ ".test";
    	   }
		try 
		{
			fragment = srcFolder.createPackageFragment(yetiPackageName, true, null);
		} catch (JavaModelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
				
				FileDialog fileDialog = new FileDialog(YetiPlugIn.getShell(),
						SWT.APPLICATION_MODAL | SWT.SAVE);

				fileDialog.setText("Save");    				
				String[] filterExt = { "*.java"};
		        fileDialog.setFilterExtensions(filterExt);
		        String selected = fileDialog.open();
		        selected = fileDialog.getFileName();
		        System.out.println(selected);
				@SuppressWarnings("unused")
				ICompilationUnit compilationUnit= fragment.createCompilationUnit(selected,autoTestCase, false, null);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
       }	
    
       
		}
    }

  
}


