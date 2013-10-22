//
// Copyright (C) 2008 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.aprop.listener;

import java.util.HashMap;
import java.util.HashSet;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.vm.AnnotationInfo;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.AALOAD;
import gov.nasa.jpf.jvm.bytecode.ALOAD;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.jvm.bytecode.PUTFIELD;
import gov.nasa.jpf.jvm.bytecode.VirtualInvocation;
import gov.nasa.jpf.vm.StackFrame;

/**
 * listener to check for @NonShared and @ConstraintShared object references
 * 
 * if a GETFIELD yielding a non-shared object refernce from a thread
 * other than the one that created this object is encountered, an
 * AssertionError is thrown. The idea is that in every thread this
 * reference leaked to, the first op has to be a GETFIELD, no matter
 * if the reference is subsequently used to call a method or do
 * a GET/PUTFIELD on a field of this object
 * 
 * while this is a simple safety property, it's implementation comes
 * at some cost because we need to store the creator thread. The
 * corresponding HashMap can grow quite large if there is a huge number
 * of non-shareds, and - even worse - the lookup has some overhead
 * for each GETFIELD and NEW
 * 
 * see the ObjectTracker for a discussion why we don't need a 
 * StateExtensionListener/Client for this
 * 
 * <2do> this could go away if we would have backtrackable per-ElementInfo
 * attribute objects, or would directly store the creator thread
 */
public class SharedChecker extends ListenerAdapter {
  
  // this is just an optimization to speed up allocation
  HashMap<ClassInfo,String[]> trackedClasses = new HashMap<ClassInfo,String[]>();
  
  // this is not really required if we only check types, not instances, but
  // we might want to extend this to specific instances (fields)
  HashMap<Integer,String[]> trackedObjects = new HashMap<Integer,String[]>();

  @Override
  public void classLoaded (VM vm, ClassInfo ci) {
    AnnotationInfo ai;
    if (ci.getAnnotation("gov.nasa.jpf.annotation.NonShared") != null) {
      trackedClasses.put(ci, new String[1]);
    } else if ((ai = ci.getAnnotation("gov.nasa.jpf.annotation.Shared")) != null) {
      String[] threadNames = ai.getValueAsStringArray();
      trackedClasses.put(ci, threadNames);
    }
  }
  
  @Override
  public void objectCreated (VM vm, ThreadInfo ti, ElementInfo ei) {
    ClassInfo ci = ei.getClassInfo();
    String[] threadNames = trackedClasses.get(ci);
    
    if (threadNames != null){
      if (threadNames[0] == null) {
        threadNames[0] = ti.getName();
      } else {
        if (!isValidThread(ti, threadNames)) {
          Instruction nextPc = ti.createAndThrowException("java.lang.AssertionError",
                                                          createErrorMessage(ti,ei,threadNames,"created"));
          ti.setNextPC(nextPc);
        }
      }
     
      trackedObjects.put(ei.getObjectRef(), threadNames);
    }
  }
  
  private boolean isValidThread(ThreadInfo ti, String[] threadNames) {
    String tn = ti.getName();
    for (int i=0; i<threadNames.length; i++) {
      if (tn.equals(threadNames[i])) {
        return true;
      }
    }
    
    return false;
  }

  @Override
  public void objectReleased (VM vm, ThreadInfo ti, ElementInfo ei) {
    // we don't bother with type checks here - if it isn't
    // a @NonShared annotated type it isn't in the HashMap anyways
    trackedObjects.remove(ei.getObjectRef());
  }

  String createErrorMessage (ThreadInfo ti, ElementInfo ei, String[] threadNames, String msg) {
    StringBuilder sb = new StringBuilder();
    sb.append("@[Non]Shared object ");
    sb.append(ei);
    sb.append("\n\t\t");
    sb.append(msg);
    sb.append(" from: ");
    sb.append(ti.getName());
    sb.append(", allowed: {");
    for (int i=0; i<threadNames.length; i++) {
      if (i>0) sb.append(',');
      sb.append(threadNames[i]);
    }
    sb.append('}');

    return sb.toString();
  }
  
  boolean checkIllegalAccess (ThreadInfo ti, ElementInfo ei) {
    if (ei != null) {
      String[] threadNames = trackedObjects.get(ei.getObjectRef());
      if (threadNames != null) {
        if (!isValidThread(ti, threadNames)) {
          Instruction nextPc = ti.createAndThrowException("java.lang.AssertionError",
                                   createErrorMessage(ti,ei,threadNames,"referenced"));
          ti.setNextPC(nextPc);
          return true;
        }
      }
    }      
    
    return false;
  }
  
  @Override
  public void instructionExecuted (VM vm, ThreadInfo ti, Instruction nextInsn, Instruction insn){
    
    if (insn instanceof GETFIELD) {
      GETFIELD get = (GETFIELD)insn;
      if (get.isReferenceField()) {
        StackFrame frame = ti.getTopFrame();
        int ref = frame.peek();
        ElementInfo ei = ti.getElementInfo(ref);
        checkIllegalAccess(ti,ei);
      }
    }
  }
}
