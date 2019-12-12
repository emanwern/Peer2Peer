import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Gui extends JFrame {

    private JLabel lblHeadline;
    private JTable peerTable;
    private Peer peer;

    private JPanel contentPane;
    private JPanel mainPanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private JPanel searchOuterPanel;
    private JPanel searchPanel;
    private JSplitPane topPanel;
    private JPanel bottomPanel;
    private JSplitPane splitPanel;

    private JTextField searchField;
    private JButton searchButton;
    private JLabel searchText;

    private final String[] COLUMN_NAMES = {"ID", "IP", "Port"};

    public Gui(String headline, Point location, Peer peer) {

        setTitle(headline);
        setSize(Utilities.getGuiSize());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.peer = peer;

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                peer.exit();

                if (Main.isStartFromConsole())
                    System.exit(0);
            }
        });

        // ContentPane
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // MainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // Headline
        lblHeadline = new JLabel(headline);
        lblHeadline.setFont(Utilities.getHeadlineFont());
        mainPanel.add(lblHeadline, BorderLayout.NORTH);

        // PeerList
        listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("ID");
        tableModel.addColumn("IP");
        tableModel.addColumn("Port");

        peerTable = new JTable(tableModel) {

            public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    //label.setHorizontalAlignment(JLabel.CENTER);
                    label.setVerticalAlignment(JLabel.CENTER);
                }
                return c;
            }
        };
        peerTable.setFont(Utilities.getNormalFont());
        peerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        peerTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        peerTable.getColumnModel().getColumn(2).setPreferredWidth(0);
        peerTable.setShowGrid(false);

        peerTable.setRowHeight(Utilities.getNormalFont().getSize() + 6);

        scrollPane = new JScrollPane(peerTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

        // Search
        searchOuterPanel = new JPanel();
        searchPanel = new JPanel();
        searchOuterPanel.setLayout(new BoxLayout(searchOuterPanel, BoxLayout.Y_AXIS));
        searchPanel.setLayout(new GridLayout(3, 1));
        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
        searchField.setToolTipText("Geben Sie eine Peer-ID ein.");
        searchField.setFont(Utilities.getNormalFont());
        searchButton = new JButton("Nach ID suchen");
        searchButton.setToolTipText("Suchen Sie im P2P-Netzwerk nach der eingegebenen ID.");
        searchButton.setFont(Utilities.getNormalFont());
        searchText = new JLabel("Ungültige ID");
        searchText.setFont(Utilities.getNormalFont());
        searchText.setForeground(Color.red);
        searchText.setVisible(false);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchForID();
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(searchText);
        searchPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        searchOuterPanel.add(searchPanel);

        // TopPanel
        topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPanel, searchOuterPanel);
        topPanel.setOneTouchExpandable(true);
        topPanel.setDividerLocation((int) (getWidth() * 0.55));
        listPanel.setMinimumSize(new Dimension(120, 0));
        searchOuterPanel.setMinimumSize(new Dimension(145, 0));
        topPanel.setBorder(null);
        topPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // BottomPanel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // SplitLayout
        splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setDividerLocation(130);
        topPanel.setMinimumSize(new Dimension(0, 85));
        bottomPanel.setMinimumSize(new Dimension(0, 150));
        splitPanel.setBorder(null);
        mainPanel.add(splitPanel, BorderLayout.CENTER);

        if (location == null)
            setLocationRelativeTo(null);
        else
            setLocation(location);
        setVisible(true);
    }

    public void setPeerList(ArrayList<PeerObject> peerListe) {

        DefaultTableModel tableModel = (DefaultTableModel) peerTable.getModel();
        if (tableModel.getRowCount() > 0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }
        tableModel.setRowCount(0);

        for (int i = 0; i < peerListe.size(); i++) {
            String[] data = new String[3];

            data[0] = "" + peerListe.get(i).getIdAsInt();
            data[1] = peerListe.get(i).getIpAsString();
            data[2] = "" + peerListe.get(i).getPortAsInt();

            tableModel.addRow(data);
        }
        tableModel.fireTableDataChanged();

        repaint();
        revalidate();
    }

    private void searchForID() {
        int id = getSearchFieldID();
        if (id != -1) {
            searchText.setVisible(false);
            searchField.setText("");
            peer.startSearch(id);
        } else {
            searchText.setVisible(true);
        }
    }

    private int getSearchFieldID() {
        if (searchText.getText().equals(""))
            return -1;
        else {
            try {
                int id = Integer.parseInt(searchField.getText().trim());
                if (id <= 65535)
                    return id;
                else
                    return -1;
            } catch (Exception e) {
                return -1;
            }
        }
    }

}
