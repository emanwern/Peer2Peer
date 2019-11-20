import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Thread st = new Thread(() -> {
                Server s = new Server();
                s.startServer();
            });
            st.start();

            Thread.sleep(1500);

            int maxNebeneinander, maxUntereinander;

            maxNebeneinander = Utilities.getScreenDimension().width / (Utilities.getGuiSize().width + 20);
            maxUntereinander = Utilities.getScreenDimension().height / (Utilities.getGuiSize().height + 20);

            AtomicInteger port = new AtomicInteger(3335);

            for (int i = 0; i < maxNebeneinander; i++) {
                for (int j = 0; j < maxUntereinander; j++) {

                    AtomicInteger stelleX = new AtomicInteger(i);
                    AtomicInteger stelleY = new AtomicInteger(j);

                    Thread t = new Thread(() -> {

                        int x, y;

                        x = 10 + stelleX.get() * (Utilities.getGuiSize().width + 10);
                        y = 10 + stelleY.get() * (Utilities.getGuiSize().height + 10);

                        Point location = new Point(x, y);
                        Peer p = new Peer(port.getAndIncrement(), location);
                        p.startPeer();
                    });
                    t.start();
                    Thread.sleep(1000);
                }
            }

            while (true) Thread.sleep(1000);

        } catch (Exception e) {
            Utilities.errorMessage(e);
        }
    }
}