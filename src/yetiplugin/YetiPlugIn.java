
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

package yetiplugin;


import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import yetiplugin.views.IOverviewArrayList;
import yetiplugin.views.ISummaryInfoArrayList;
import yetiplugin.views.OverviewArrayList;
import yetiplugin.views.SummaryInfoArrayList;



/**
 * The activator class controls the plug-in life cycle
 */
public class YetiPlugIn extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "YetiPlugIn";
	
	public static boolean realtimeGUI = false;
	
	
	public static IOverviewArrayList overview =  null;  // new OverviewArrayList();
	public static ISummaryInfoArrayList summaryList = null; //new SummaryInfoArrayList();
	
	// The shared instance
	private static YetiPlugIn plugin;
	
	/**
	 * The constructor
	 */
	public YetiPlugIn() {
		plugin = this;
	}
	

	
	public static boolean isRealtimeGUI() {
		return realtimeGUI;
	}


	public static void setRealtimeGUI(boolean realtimeGUI) {
		YetiPlugIn.realtimeGUI = realtimeGUI;
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		YetiPlugIn.getSummaryInfoArrayList();
		YetiPlugIn.getOverviewArrayList();
	}
	
	/**
	 * @param relativePath
	 *            workspace relative path
	 * @return given path if path is not known in workspace
	 */
	public static IPath relativeToAbsolute(IPath relativePath) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(relativePath);
		if (resource != null) {
			return resource.getLocation();
		}
		return relativePath;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static YetiPlugIn getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	  /**
     * @return an instance of the string table
     */
	

	public static IOverviewArrayList getOverviewArrayList() {
		try
		{	
			if (overview == null)
			{
		  	overview = new OverviewArrayList();
			 }
		}
		catch (Exception e) {
			
			}
		return overview;
	}
	
	
	public static ISummaryInfoArrayList getSummaryInfoArrayList() {
		try
		{	
			if (summaryList == null)
			{
				summaryList = new SummaryInfoArrayList();
			 }
		}
		catch (Exception e) {
			
			}
		return summaryList;
	}
	
	
	public static Shell getShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}
		return window.getShell();
	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		if(Display.getCurrent() != null) {
			return getDefault().getWorkbench().getActiveWorkbenchWindow();
		}
		// need to call from UI thread
		final IWorkbenchWindow [] window = new IWorkbenchWindow[1];
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				window[0] = getDefault().getWorkbench().getActiveWorkbenchWindow();
			}
		});
		return window [0];
	}

}
