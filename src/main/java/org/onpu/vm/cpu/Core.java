package org.onpu.vm.cpu;

import org.onpu.vm.process.Process;

public class Core {
   private boolean isFree=true;
   private Process currentProcess = null;

   public String getState() {
      String _tmp = " ";
      if(this.isFree)
         _tmp+="is free";
      else
         _tmp+= "Working on Process: "+currentProcess;

      return _tmp ;
   }

   public Process getCurrentProcess(){return currentProcess;}

   public void setProcess(Process process) {
      currentProcess = process;
      isFree = false;
   }

   public void setFree() {
      currentProcess = null;
      isFree = true;
   }

   public boolean isFree() {
      return isFree;
   }
}
