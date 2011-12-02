package yeti.environments.cofoja;

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

import java.lang.reflect.Constructor;

import yeti.YetiCard;
import yeti.YetiModule;
import yeti.YetiName;
import yeti.YetiType;
import yeti.environments.java.YetiJavaConstructor;

/**
 * Class that represents a constructor in Java annotated with CoFoJa.
 * @author  Vasileios Dimitriadis (vd508@cs.york.ac.uk, vdimitr@hotmail.com)
 * @author  Manuel Oriol (manuel@cs.york.ac.uk)
 * @date  13 Jul 2011
 */
public class YetiCoFoJaConstructor extends YetiCoFoJaRoutine {
	
	/**
	 * The wrapped Java constructor.
	 */
	private YetiJavaConstructor javaConstructor = null;
	
	/**
	 * Constructor to define a CoFoJa constructor.
	 * 
	 * @param name the name of this constructor.
	 * @param openSlots the open slots for this constructor.
	 * @param returnType the returnType of this constructor.
	 * @param originatingModule the module in which it was defined.
	 * @param constructor the constructor itself.
	 */
	public YetiCoFoJaConstructor(YetiName name, YetiType[] openSlots, YetiType returnType, YetiModule originatingModule, Constructor<?> constructor) {
		super(name, openSlots, returnType, originatingModule);
		javaConstructor = new YetiJavaConstructor(name, openSlots, returnType, originatingModule, constructor);
		acceptableExceptionTypes = javaConstructor.getAcceptableExceptionTypes();
	}
	
	@Override
	public String makeEffectiveCall(YetiCard[] yetiCards) throws Throwable {
		return javaConstructor.makeEffectiveCall(yetiCards);
	}
	
	@Override
	public String toString() {
		return javaConstructor.toString();
	}
}
