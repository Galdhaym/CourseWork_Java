package org.onpu.vm.cpu;

import org.onpu.vm.time.ClockListener;
import org.onpu.vm.process.Process;
import org.onpu.vm.VirtualMachine;

public class CPU implements ClockListener
{
    Core[] cores;

    public CPU(final int number)
    {
        this.cores = new Core[number];

        for(int i = 0; i<number; i++)
            cores[i]=new Core();
    }
    public Core[] getCores(){return cores;}

    @Override
    public String toString() {
        String _tmp=" ";
        for (int i =0; i<cores.length; i++)
            _tmp+="\t{Core#"+i+"\t"+cores[i].getState()+"}";
        return "CPU{" +_tmp+"}";
    }

    public void setCoreJob(int coreNumb, Process process)
    {
        if(process!=null) {
            cores[coreNumb].currentProcess = process;
            cores[coreNumb].isFree = false;
        }
    }

public int getFreeCore()
{
    for (int i =0; i<cores.length; i++)
        if(cores[i].isFree)
            return i;
    return -1;
}






    @Override
    public void onTick() {
        for(Core core : cores)
        {
            if(!core.isFree) {
                Process _tmProcess = core.getCurrentProcess();

                _tmProcess.setBursTime(_tmProcess.getBursTime() + 1);
                if (_tmProcess.getBursTime() == _tmProcess.getTickWorks()) {
                    VirtualMachine.PDone(core.currentProcess);
                    core.currentProcess = null;
                    core.isFree = true;
                }
            }
        }
    }
}
