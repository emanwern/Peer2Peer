import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Gui<T> extends JFrame {

    private JLabel lblHeadline;
    private JTextArea peerList;

    public Gui(String headline, Point location, T object) {

        setTitle(headline);
        setSize(Utilities.getGuiSize());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                dispose();
                if (object instanceof Peer)
                    ((Peer) object).exit();
                else if (object instanceof Server)
                    ((Server) object).exit();
            }
        });

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);

        lblHeadline = new JLabel(headline);
        lblHeadline.setFont(Utilities.getHeadlineFont());
        mainPanel.add(lblHeadline, BorderLayout.NORTH);

        peerList = new JTextArea();
        peerList.setFont(Utilities.getNormalFont());
        peerList.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(peerList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        if (location == null)
            setLocationRelativeTo(null);
        else
            setLocation(location);
        setVisible(true);
    }

    public void setPeerList(String ausgabe) {
        peerList.setText(ausgabe);
        repaint();
        revalidate();
    }

}
