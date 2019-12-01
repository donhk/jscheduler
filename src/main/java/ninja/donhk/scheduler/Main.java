package ninja.donhk.scheduler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Agendable agendable1 = new Agendable() {
            @Override
            public void execute() {
                System.out.println("testing1 " + this.getClass().getName() + " " + LocalDateTime.now());
                try {
                    TimeUnit.MILLISECONDS.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            @Override
            public long frequency() {
                return 10000L;
            }
        };
        Agendable agendable2 = new Agendable() {
            @Override
            public void execute() {
                System.out.println("testing2 " + this.getClass().getName() + " " + LocalDateTime.now());
                try {
                    TimeUnit.MILLISECONDS.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            @Override
            public long frequency() {
                return 9000L;
            }
        };
        final JScheduler scheduler = JScheduler.newAutoScalableScheduler();
        scheduler.schedule(agendable1)
                .schedule(agendable2)
                .start();
    }
}
