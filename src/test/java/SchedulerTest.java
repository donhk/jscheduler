import ninja.donhk.scheduler.Agendable;
import ninja.donhk.scheduler.JScheduler;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author donhk
 */
public class SchedulerTest {
    @Test
    public void test1() {
        Agendable agendable1 = new Agendable() {
            @Override
            public void execute() {
                System.out.println("testing " + this.getClass().getName() + " " + LocalDateTime.now());
                try {
                    TimeUnit.MILLISECONDS.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            @Override
            public long frequency() {
                return 1000L;
            }
        };
        final JScheduler scheduler = JScheduler.newAutoScalableScheduler();
        scheduler.schedule(agendable1);
        scheduler.start();
    }
}
