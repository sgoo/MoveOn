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

package gov.nasa.jpf.aprop;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ClassLoaderInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;

import java.util.logging.Logger;

public class ContractContext {

  ThreadInfo ti;
  MethodInfo mi;

  Logger logger;

  public ContractContext (Logger logger){
    this.logger = logger;
  }

  public void setContextInfo (MethodInfo mi, ThreadInfo ti) {
    this.ti = ti;
    this.mi = mi;
  }

  public Logger getLogger() {
    return logger;
  }

  public ClassInfo resolveClassInfo (String clsName) {

    ClassInfo ci = ClassLoaderInfo.getCurrentResolvedClassInfo(clsName);

    if (ci == null) {
      if (clsName.indexOf('.') < 0){  // clsName didn't have a package
        ClassInfo mci = mi.getClassInfo();
        String pkgPrefix = mci.getPackageName();

        if (pkgPrefix != null) { // same package?
          String cn = pkgPrefix + '.' + clsName;
          ci = ClassLoaderInfo.getCurrentResolvedClassInfo(cn);

          if (ci == null) { // inner class?
            cn = mci.getName() + '$' + clsName;
            ci = ClassLoaderInfo.getCurrentResolvedClassInfo(cn);

            if (ci == null) { // last resort: try java.lang
              ci = ClassLoaderInfo.getCurrentResolvedClassInfo("java.lang." + clsName);
            }
          }
        }
      }
    }

    return ci;
  }

  public ThreadInfo getThreadInfo() {
    return ti;
  }

  MethodInfo getMethodInfo() {
    return mi;
  }

  String getPackage () {
    return mi.getClassInfo().getPackageName();
  }
}
