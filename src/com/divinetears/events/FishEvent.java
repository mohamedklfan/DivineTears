package com.divinetears.events;

import com.divinetears.GUI;
import com.divinetears.node.Job;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

public class FishEvent extends Job {


    @Override
    public int delay() {
        return 250;
    }

    private final Player player = ctx.players.local();
    private final Timer t = new Timer(4000);
    public Npc myPool = ctx.npcs.getNil();

    public FishEvent(MethodContext ctx) {
        super(ctx);
    }

    private boolean collecting() {
        return myPool.isValid() && interactingTile().equals(myPool.getLocation());
    }

    private Tile interactingTile() {
        final double orientation = Math.toRadians(player.getOrientation());
        return player.getLocation().derive((int) Math.round(Math.cos(orientation)), (int) Math.round(Math.sin(orientation)));

    }

    @Override
    public boolean activate() {
        if (GUI.fish) {
            if (myPool == ctx.npcs.getNil() || !myPool.isValid()) {
                for (Npc pool : ctx.npcs.select().id(17792).nearest().first()) {
                    myPool = pool;

                }
            }
        }
        return myPool != ctx.npcs.getNil() && myPool.isValid();
    }

    private void waitToMove() {
        t.reset();
        while (t.isRunning() && (!player.isInMotion() || !collecting())) {
            if (!myPool.isValid()) {
                break;
            }
            Delay.sleep(50, 100);
        }
    }

    @Override
    public void execute() {
        if (!collecting()) {
            if (ctx.movement.getDistance(myPool.getLocation(), player.getLocation()) > 7) {
                ctx.movement.findPath(myPool.getLocation()).traverse();
            }
            if (!myPool.isOnScreen()) {
                ctx.camera.turnTo(myPool);
                if (!myPool.isOnScreen()) {
                    ctx.camera.setYaw(Random.nextInt(12, 27));
                }
            }

            myPool.interact("Net");
            if (myPool.interact("Net")) {
                waitToMove();
            }
        }
        while (collecting()) {
            if (myPool.isValid()) Delay.sleep(50, 100);
        }

    }
}
