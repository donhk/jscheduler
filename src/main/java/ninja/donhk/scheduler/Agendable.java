package ninja.donhk.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author donhk
 */
public abstract class Agendable implements Runnable {

    private LocalDateTime lastRun = LocalDateTime.now();
    private boolean running = false;

    @Override
    public void run() {
        running = true;
        execute();
        running = false;
        lastRun = LocalDateTime.now();
    }

    /**
     * method to run every {@link #frequency()}
     */
    public abstract void execute();

    /**
     * @return millis for next run
     */
    public abstract long frequency();

    /**
     * @return timestamp of next run
     */
    public LocalDateTime nextRun() {
        return lastRun.plus(frequency(), ChronoUnit.MILLIS);
    }

    /**
     * @return true if the method is running
     */
    public boolean isRunning() {
        return running;
    }
}
