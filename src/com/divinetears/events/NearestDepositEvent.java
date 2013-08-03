package com.divinetears.events;

import com.divinetears.GUI;
import com.divinetears.Global;
import com.divinetears.node.Job;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Npc;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

public class NearestDepositEvent extends Job {


    private final Player player = ctx.players.local();
    private final Timer t = new Timer(4000);
    private Npc myPool = ctx.npcs.getNil();
    private GameObject myObject = ctx.objects.getNil();
    private final double distanceFromPool = player.getLocation().distanceTo(myPool.getLocation());
    private final double distanceFromObject = player.getLocation().distanceTo(myObject.getLocation());

    public NearestDepositEvent(MethodContext ctx) {
        super(ctx);
    }

    private boolean collecting() {
        if (myPool.isValid() && interactingTile().equals(myPool.getLocation())) {
            return true;
        } else if (myObject.isValid() && interactingTile().equals(myObject.getLocation())) {
            return true;
        }
        return false;
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
        if (GUI.nearestDeposit) {
            for (GameObject r : ctx.objects.select().id(Global.FORMATION, Global.ROCK).nearest().first()) {
                myObject = r;
            }
            for (Npc pool : ctx.npcs.select().id(Global.POOL).nearest().first()) {
                myPool = pool;

            }
        }
        if (distanceFromPool < distanceFromObject) {
            return myPool != ctx.npcs.getNil() && myPool.isValid();
        } else {
            return myObject != ctx.objects.getNil() && myObject.isValid();
        }


    }

    private void waitToMove() {
        t.reset();
        while (t.isRunning() && (!player.isInMotion() || !collecting())) {
            if (!myPool.isValid()) {
                break;
            }
            sleep(50, 100);
        }
    }

    @Override
    public void execute() {
        if (distanceFromPool < distanceFromObject) {
            if (player.getLocation().distanceTo(myPool) > 8) ctx.movement.stepTowards(myPool);
            if (!myPool.isOnScreen()) {
                ctx.camera.turnTo(myPool);

            }


            if (myPool.interact("Net")) {
                waitToMove();
            }
        } else {
            if (player.getLocation().distanceTo(myObject) > 8) ctx.movement.stepTowards(myObject);

            if (!myObject.isOnScreen()) {
                ctx.camera.turnTo(myObject);

            }


            if (myObject.interact("Mine") && myObject.getId() == Global.ROCK || myObject.interact("Chop down") && myObject.getId() == Global.FORMATION) {
                waitToMove();
            }
        }


        while (collecting()) {
            if (!idle()) {
                Global.checkIfIdle.reset();
                sleep(50, 100);
            } else {
                if (Global.checkIfIdle.isRunning()) {
                    sleep(50, 100);
                } else {
                    break;
                }
            }
        }

    }
}
