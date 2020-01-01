import javax.swing.*;
import java.awt.*;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Utilities {

    public static String getServerIp() {
        return Variables.getStringValue("server_ip");
    }

    public static void setServerIp(String serverip) {
        Variables.putObject("server_ip", serverip);
    }

    public static int getStandardPort() {
        return Variables.getIntValue("standard_port");
    }

    public static int getPeerPackLength() {
        return Variables.getIntValue("peer_pack_length");
    }

    public static String getMyIpAsString() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            errorMessage(e);
        }
        return null;
    }

    public static void printMyIp() {
        System.out.println(getMyIpAsString() + "\n");
    }

    public static void errorMessage(Exception e) {
        if (isShowGui())
            JOptionPane.showMessageDialog(null, e.toString());
        e.printStackTrace();
    }

    public static byte[] charToByteArray(char i) {

        byte[] result = new byte[2];
        result[0] = (byte) (i >> 8);
        result[1] = (byte) (i);
        return result;
    }

    public static char byteArrayToChar(byte[] array) {
        return (char) (array[1] | array[0] << 8);
    }

    public static void printByteArrayAsBinaryCode(byte[] array) {

        for (byte b : array) {
            System.out.print(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0') + " ");
        }
        System.out.println();
    }

    public static boolean isArrayEmty(byte[] array) {

        for (byte b : array) {

            if (b != 0)
                return false;
        }
        return true;
    }

    public static InetAddress getInetAdressFromString(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (Exception e) {
            errorMessage(e);
        }
        return null;
    }

    public static InetAddress getInetAdressFromByteArray(byte[] ip) {
        try {
            return InetAddress.getByAddress(ip);
        } catch (Exception e) {
            errorMessage(e);
        }
        return null;
    }

    public static byte[] getMyIpAsByteArray() {
        try {
            return InetAddress.getLocalHost().getAddress();
        } catch (Exception e) {
            errorMessage(e);
        }
        return null;
    }

    public static void printPeerList(Gui gui, ArrayList<PeerObject> peerListe, boolean printOnConsole) {
        if (isShowGui())
            gui.setPeerList(peerListe);
        if (printOnConsole)
            System.out.println("Anzahl Peers in der Liste: " + peerListe.size());
    }

    public static int getScreenUpdateTime() {
        return Variables.getIntValue("screen_update_time");
    }

    public static String byteArrayToIp(byte[] ipAsByteArray) {
        try {
            InetAddress i = Inet4Address.getByAddress(ipAsByteArray);
            return i.getHostAddress();
        } catch (Exception e) {
            errorMessage(e);
        }
        return null;
    }

    public static void switchDefault() {
        System.err.println("Im Switch wurde ein falscher Parameter übergeben");
    }

    public static void printTimestamp(long timestamp) {

        SimpleDateFormat s = new SimpleDateFormat("d.M.y H:m:s S");
        System.out.println(s.format(new Date(timestamp)));
    }

    public static byte[] addAll(final byte[] array1, byte[] array2) {
        byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static void fehlermeldungBenutzerdefiniert(String s) {
        System.err.println(s);
    }

    public static void fehlermeldungVersion() {
        fehlermeldungBenutzerdefiniert("Falsche Version übergeben");
    }

    public static byte[] getServerIpAsByteArray() {
        return getInetAdressFromString(getServerIp()).getAddress();
    }

    public static Dimension getGuiSize() {
        return (Dimension) Variables.getObject("gui_size");
    }

    public static Font getNormalFont() {
        return (Font) Variables.getObject("font");
    }

    public static Font getHeadlineFont() {
        return (Font) Variables.getObject("font_headline");
    }

    public static Dimension getScreenDimension() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static byte[] getStandardPortAsByteArray() {
        return charToByteArray((char) getStandardPort());
    }

    public static void modificationException(Exception e) {
        System.err.println("Modification-Exception");
        //e.printStackTrace();
    }

    public static void setShowGui(boolean showGui) {
        Variables.putObject("show_gui", showGui);
    }

    public static boolean isShowGui() {
        return (Boolean) Variables.getObject("show_gui");
    }

    private static String getTimeStamp() {
        return "(" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ")";
    }

    //TODO Msg-Panel
    private static void printOnGuiMsgPanel(Peer i, String s) {
        Gui g = i.getGui();
        if (isShowGui() && g != null)
            g.addText(s);
    }

    private static void printOnGuiLogPanel(Peer i, String s) {
        Gui g = i.getGui();
        if (isShowGui() && g != null)
            g.addText(getTimeStamp() + " " + s);
    }

    private static void printOnConsole(Peer i, String s) {
        System.out.println(i.getMyPeer().getIdAsInt() + ": " + getTimeStamp() + " " + s);
    }

    //Consolenausgaben; i = mein Peer-Objekt
    public static void printLogInformation(Peer i, String s) {
        printOnGuiLogPanel(i, s);
        printOnConsole(i, s);
    }

    public static void printMsg(Peer i, String s) {
        printOnGuiMsgPanel(i, s);
        printOnConsole(i, s);
    }

    public static void printMetaInfo(Peer i, PeerObject from, int tag, int version) {
        String s = "[Von ID " + from.getIdAsInt() + "] Tag " + tag + " in Version " + version + " erhalten";
        printOnGuiLogPanel(i, s);
        printOnConsole(i, s);
    }

    //TODO
    public static void setLeader(Peer i, PeerObject leader) {
        printLogInformation(i, "Leader ist " + leader.getIdAsInt());
        i.getGui().setHeadline(i.getGui().getTitle() + "; Leader: " + leader.getIdAsInt());
    }

    public static void connectException(ConnectException c) {
        System.err.println("Connection refused: connect");
    }
}
