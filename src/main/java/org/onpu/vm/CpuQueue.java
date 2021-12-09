package org.onpu.vm;

import org.onpu.vm.memory.MemoryManager;
import org.onpu.vm.process.Process;
import org.onpu.vm.process.Status;
import org.onpu.vm.time.ClockGenerator;

import java.util.ArrayList;

public class CpuQueue {
    private final ArrayList<Process> processes;

    public CpuQueue() {
        this.processes = new ArrayList<>();
    }

    public boolean addProcess(Process p) {
        if (MemoryManager.allocateMemory(p)) {
            this.processes.add(p);
            return true;
        }

        return false;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public Process pullNextProcess() {
        int i = findMaxPriorityProcessIndex();
        if (i == -1) {
            return null;
        }

        Process process = processes.get(i);
        processes.remove(i);

        return process;
    }

    public int findMaxPriorityProcessIndex() {
        int maxPriority = 0;
        int processIndex = -1;
        for (int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getPriority() > maxPriority){
                maxPriority = processes.get(i).getPriority();
                processIndex = i;
            };
        }

        return processIndex;
    }
}
