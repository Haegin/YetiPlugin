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

/**
 * Class that represents an exception or an error thrown during a call.
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  Jul 18, 2009
 */
@SuppressWarnings("serial")
public class YetiCallException extends Exception {
	
	/**
	 * The log of the current call.
	 */
	public String log;
	
	/**
	 * The original Throwable for the exception. 
	 */
	public Throwable originalThrowable;
	
	
	/**
	 * Simple getter for the logs.
	 * @return  the logs for the throwable.
	 */
	public String getLog() {
		return log;
	}

	/**
	 * Simple getter for the Throwable
	 * @return  the Throwable originally thrown.
	 */
	public Throwable getOriginalThrowable() {
		return originalThrowable;
	}


	/**
	 * Simple constructor for the Exception.
	 * 
	 * @param log the log at the moment of the error.
	 * @param originalThrowable the original Throwable.
	 */
	public YetiCallException(String log, Throwable originalThrowable) {
		super();
		this.log = log;
		this.originalThrowable = originalThrowable;
	}
	
	
	

}
