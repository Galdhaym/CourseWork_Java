package org.onpu.vm;

import org.onpu.Main;
import org.onpu.vm.cpu.CPU;
import org.onpu.vm.memory.MemoryManager;
import org.onpu.vm.process.Process;
import org.onpu.vm.process.Status;
import org.onpu.vm.time.ClockGenerator;
import org.onpu.vm.time.ClockListener;

import java.util.ArrayList;

public class VirtualMachine implements ClockListener {
    static ArrayList<Process> doneProcesses;
    static Queue queue;

    CPU cpu;
    MemoryManager memoryManager;
    ClockGenerator clockGenerator;

    public VirtualMachine() {
        queue = new Queue();

        doneProcesses = new ArrayList<>();

        this.cpu = new CPU(Configuration.coreCount);
        this.memoryManager = new MemoryManager();
        this.clockGenerator = new ClockGenerator();

        this.clockGenerator.addListener(cpu);
        this.clockGenerator.addListener(this);
    }

    public void Start()
    {
        preLaunchInit();
        this.clockGenerator.run();
    }

    private void preLaunchInit()
    {
//        MemoryManager.addMemoryBlock(new MemoryBlock(0,100,null));
//
        queue.Add(Configuration.initPCount);
    }

    private void addJob()
    {
        if(Utils.getRandBool()) {
            queue.Add(Utils.getRandInt(Configuration.minValue));
        }
        updateTable();
    }


    @Override
    public String toString() {
        return "VirtualMachine{\n"+cpu+'\n'+ memoryManager +'\n'+queue+"\nDone:"+doneProcesses+"\n}";
    }

    public static void PDone(Process process)
    {
        if(Utils.getRandBool()) {
            process.setStatus(Status.Finished);
            doneProcesses.add(process);
        }
        else
        {
            process.setStatus(Status.Waiting);
            queue.addProcess(process);
        }
    }

    private void clearOutdated()
    {
        if(ClockGenerator.getTick()% Configuration.rmOldPIterator ==0) {
            queue.cancelOutdated();
        }
    }

    private void setJobToCPU()
    {
        for (int i = 0; i< Configuration.coreCount; i++) {
            int _tmpInt = cpu.getFreeCore();
            if (_tmpInt >= 0) {
                cpu.setCoreJob(_tmpInt, queue.getNextProcess());
            }
        }
    }

    public void updateTable()
    {
        Main.controller.updateTable(queue,doneProcesses);}

    @Override
    public void onTick() {
        clearOutdated();
        setJobToCPU();
        addJob();
    }
}
