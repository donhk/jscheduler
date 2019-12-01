package ninja.donhk.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author donhk
 */
public class JScheduler {

    private final ExecutorService executorService;
    private final List<Agendable> jobs = new ArrayList<>();
    private final List<Future<?>> running = Collections.synchronizedList(new ArrayList<>());
    private Thread loop = null;

    private JScheduler(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static JScheduler newAutoScalableScheduler() {
        return new JScheduler(Executors.newCachedThreadPool());
    }

    public static JScheduler newScheduler(int maxEvents) {
        return new JScheduler(Executors.newFixedThreadPool(maxEvents));
    }

    public JScheduler schedule(Agendable agendable) {
        jobs.add(agendable);
        return this;
    }

    public int running() {
        return running.size();
    }

    private void invoke() {
        final LocalDateTime now = LocalDateTime.now();
        for (Agendable event : jobs) {
            final long nextRun = now.until(event.nextRun(), ChronoUnit.MILLIS);
            if (nextRun <= 0 && !event.isRunning()) {
                running.add(executorService.submit(event));
            }
        }
    }

    public void start() {
        loop = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                invoke();
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    running.removeIf(f -> f.isDone() || f.isCancelled());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "loop");
        loop.start();
    }

    public void stop() {
        if (loop != null) {
            loop.interrupt();
        }
        executorService.shutdownNow();
    }
}
