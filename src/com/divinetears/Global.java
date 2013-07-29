package com.divinetears;

import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;

public class Global {

    public static final String obeliskCharging = "";
    public static final Timer checkIfIdle = new Timer(Random.nextInt(8500, 10000));
    public static final int ROCK = 86545;
    public static final int FORMATION = 85894;
    public static final int POOL = 17792;

    public static enum Obelisk {
        SARADOMIN(85319, "Saradomin"),
        ZAMORAK(55318, "Zamorak");
        public final int id;
        public final String name;

        Obelisk(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}
