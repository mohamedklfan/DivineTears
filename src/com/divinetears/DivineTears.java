package com.divinetears;


import com.divinetears.events.FishEvent;
import com.divinetears.events.MineChopEvent;
import com.divinetears.node.Job;
import com.divinetears.node.JobContainer;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.Delay;

@Manifest(authors = "lolek0120", name = "DivineTears", description = "Collects divine tears")
public class DivineTears extends PollingScript {


    private final GUI frame = new GUI();
    private JobContainer container = null;

    public DivineTears() {
        getExecQueue(State.START).add(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
                while(frame.isActive()) sleep(350);
                if (container == null) {
                    container = new JobContainer(new Job[]{
                            new MineChopEvent(getContext()),
                            new FishEvent(getContext())

                    });

                }
            }
        });
    }


    @Override
    public int poll() {
       while (frame.isActive()) Delay.sleep(500);
        final Job job = container.get();
        if (job != null) {
            job.execute();
            return job.delay();
        }
        return 250;
    }

    @Override
    public void stop() {
        System.out.println("Script Stopped");
    }
}