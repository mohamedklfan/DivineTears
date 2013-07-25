package com.divinetears;


import com.divinetears.events.MiningEvent;
import com.divinetears.node.Job;
import com.divinetears.node.JobContainer;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;

@Manifest(authors = "lolek0120", name = "DivineTears", description = "Collects divine tears")
public class DivineTears extends PollingScript {


    private JobContainer container = null;
    public DivineTears() {
        getExecQueue(State.START).add(new Runnable() {
            @Override
            public void run() {
                if (container == null) {
                    container = new JobContainer(new Job[]{
                            new MiningEvent(getContext())
                    });

                }
            }
        });
    }


    @Override
    public int poll() {
        final Job job = container.get();
        if (job != null) {
            job.execute();
            return job.delay();
        }
        return 250;
}
}