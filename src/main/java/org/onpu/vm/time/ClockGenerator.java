package org.onpu.vm.time;

import org.onpu.vm.Configuration;

import java.util.ArrayList;
import java.util.TimerTask;

public class ClockGenerator extends TimerTask {
    ArrayList<ClockListener> listenersList = new ArrayList<>();

    private static int tick;
    public static int getTick() {
        return tick;
    }
    public static void TickUP(){
        tick++;
    }
    public static void clearTime(){tick =0;}

    @Override
    public void run() {
        while (true)
        {
            try {
                Thread.sleep(1000, Configuration.tickIncrement);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TickUP();

            for(ClockListener listener : listenersList)
                listener.onTick();

        }
    }

    public void addListener(ClockListener listener)
    {
        listenersList.add(listener);
    }
}
