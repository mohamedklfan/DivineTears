package com.divinetears.events;

import com.divinetears.GUI;
import com.divinetears.node.Job;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Delay;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

public class MineChopEvent extends Job {

    private GameObject myObject = ctx.objects.getNil();
    private final Player player = ctx.players.local();
    private final Timer t = new Timer(4000);

    @Override
    public int delay() {
        return 250;
    }

    public MineChopEvent(MethodContext ctx) {
        super(ctx);
    }

    private boolean collecting() {
        return myObject.isValid() && interactingTile().equals(myObject.getLocation());
    }

    private Tile interactingTile() {
        final double orientation = Math.toRadians(player.getOrientation());
        return player.getLocation().derive((int) Math.round(Math.cos(orientation)), (int) Math.round(Math.sin(orientation)));

    }

    private void waitToMove() {
        t.reset();
        while (t.isRunning() && (!player.isInMotion() || !collecting())) {
            if (!myObject.isValid()) {
                break;
            }
            Delay.sleep(50, 100);
        }
    }

    private Timer checkIfIdle = new Timer(Random.nextInt(8500, 10000));

    public boolean idle() {
        return !player.isInMotion() && !player.isInCombat() && player.getAnimation() == -1;
    }

    @Override
    public boolean activate() {
        if (!GUI.fish) {
            if (myObject == ctx.objects.getNil() || !myObject.isValid()) {
                for (GameObject r : ctx.objects.select().name(GUI.objectToCollectFrom).nearest().first()) {
                    myObject = r;
                }
            }
        }
        return myObject != ctx.objects.getNil() && myObject.isValid();
    }

    @Override
    public void execute() {
        if (!collecting()) {
            if (ctx.movement.getDistance(myObject.getLocation(), player.getLocation()) > 7) {
                ctx.movement.findPath(myObject.getLocation()).traverse();
            }
            if (!myObject.isOnScreen()) {
                ctx.camera.turnTo(myObject);
                if (!myObject.isOnScreen()) {
                    ctx.camera.setYaw(Random.nextInt(12, 27));
                }
            }

            myObject.interact(GUI.interact);
            if (myObject.interact(GUI.interact)) {
                waitToMove();
            }
        }
        while (collecting()) {
            if (!idle()) {
                if (!checkIfIdle.isRunning()) checkIfIdle.reset();
                Delay.sleep(50, 100);
            } else {
                if (checkIfIdle.isRunning()) {
                    Delay.sleep(50, 100);
                } else {
                    break;
                }
            }
        }


    }
}
