package org.cis1200.tether;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class PowerUp {

    private Instant startTime;
    private boolean consumed;
    private int duration;
    private PowerUpType type;

    public PowerUp(int duration, PowerUpType powerUpType) {
        this.type = powerUpType;
        this.duration = duration;
        startTime = Instant.now();
        consumed = false;
    }

    public int secondsLeft() {
        if (consumed) {
            return -1;
        }
        return (int) (duration - Duration.between(startTime, Instant.now()).toSeconds());
    }

    public void consume() {
        consumed = true;
    }

    public PowerUpType getType() {
        return type;
    }

    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PowerUp powerUp = (PowerUp) o;
        return type == powerUp.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    public enum PowerUpType {
        DOUBLE_JUMP,
        DASH,
        UNTETHER
    }
}
