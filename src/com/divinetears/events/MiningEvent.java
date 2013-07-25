package com.divinetears.events;

import com.divinetears.node.Job;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

public class MiningEvent extends Job {

    public MiningEvent(MethodContext ctx) {
        super(ctx);
    }

    private static final String rockName = "Divine Tear Rocks";
    private static GameObject myRock = null;
    private final Player player = ctx.players.local();
    private final Timer t = new Timer(4000);

    private boolean mining() {
        return /*player.getAnimation() != -1 && */myRock.isValid() && interactingTile().equals(myRock.getLocation());
    }

    private Tile interactingTile() {
        final double orientation = Math.toRadians(player.getOrientation());
        return player.getLocation().derive((int) Math.round(Math.cos(orientation)), (int) Math.round(Math.sin(orientation)));

    }

    private void waitToMove() {
        t.reset();
        while (t.isRunning() && (!player.isInMotion() || !mining())) {
            if (!myRock.isValid()) {
                break;
            }
            Delay.sleep(50, 100);
        }
    }

    public boolean idle() {
        return !player.isInMotion() && !player.isInCombat() && player.getAnimation() == -1;
    }

    @Override
    public boolean activate() {
        if (myRock == null || !myRock.isValid()) {
            for (GameObject r : ctx.objects.name(rockName).nearest().first()) {
                myRock = r;
            }
        }

        return myRock != null && myRock.isValid();
    }

    @Override
    public void execute() {
        if (!mining()) {
            if (!myRock.isOnScreen()) {
                ctx.camera.turnTo(myRock);
            }

            myRock.interact("Mine");
            if (myRock.interact("Mine")) {
                waitToMove();
            }
        }
        while (mining()) {
            if (myRock.isValid()) Delay.sleep(50, 100);
        }


    }
}
