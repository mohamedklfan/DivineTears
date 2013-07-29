package com.divinetears.events;

import com.divinetears.GUI;
import com.divinetears.Global;
import com.divinetears.node.Job;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

public class FishEvent extends Job {

    private final Player player = ctx.players.local();
    private final Timer t = new Timer(4000);
    private Npc myPool = ctx.npcs.getNil();

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

    public boolean idle() {
        return !player.isInMotion() && !player.isInCombat() && player.getAnimation() == -1;
    }

    @Override
    public boolean activate() {
        if (GUI.fish && !GUI.nearestDeposit) {
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

        if (player.getLocation().distanceTo(myPool) > 8) {
            ctx.movement.stepTowards(myPool.getLocation());
        }
        if (!myPool.isOnScreen()) {
            ctx.camera.turnTo(myPool);
        }
        if (myPool.interact("Net")) {
            waitToMove();
        }

        while (collecting()) {
            if (!idle()) {
                Global.checkIfIdle.reset();
                Delay.sleep(50, 100);
            } else {
                if (Global.checkIfIdle.isRunning()) {
                    Delay.sleep(50, 100);
                } else {
                    break;
                }
            }
        }
    }
}
