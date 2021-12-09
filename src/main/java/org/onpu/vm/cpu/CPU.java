package org.onpu.vm.cpu;

import org.onpu.vm.CpuQueue;
import org.onpu.vm.time.ClockListener;
import org.onpu.vm.process.Process;
import org.onpu.vm.VirtualMachine;

public class CPU implements ClockListener {
    Core[] cores;
    VirtualMachine vm;

    public CPU(final int number, VirtualMachine vm) {
        this.cores = new Core[number];
        this.vm = vm;

        for (int i = 0; i < number; i++)
            cores[i] = new Core();
    }

    public Core[] getCores() {
        return cores;
    }

    @Override
    public String toString() {
        String _tmp = " ";
        for (int i = 0; i < cores.length; i++)
            _tmp += "\t{Core#" + i + "\t" + cores[i].getState() + "}";
        return "CPU{" + _tmp + "}";
    }

    @Override
    public void onTick() {
        for (Core core : cores) {
            if (!core.isFree()) {
                Process process = core.getCurrentProcess();
                process.incExecutedTicks();

                if (process.isCompleted()) {
                    vm.terminateProcess(core.getCurrentProcess());
                    core.setFree();
                }
            }

            if (core.isFree()) {
                Process process = vm.getCpuQueue().pullNextProcess();
                if (process != null) {
                    core.setProcess(process);
                    process.start();
                }
            }
        }
    }
}
