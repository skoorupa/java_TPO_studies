/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ChatClient extends Thread {
    private String host;
    private int port;
    private String id;
    private SocketChannel socket;
    //
    private static ByteBuffer inBuf = ByteBuffer.allocateDirect(1024);
    private static Charset charset = StandardCharsets.UTF_8;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void login() throws IOException {
        socket = SocketChannel.open();
        socket.configureBlocking(false);
        socket.connect(new InetSocketAddress(host, port));
        start();
    }

    public void logout() {
        interrupt();
    }

    public void send(String req) {

    }

    public void run() {
        while (!isInterrupted()) {
            // ...
        }
    }

    // nieblokujące wejście - wyjście
    public String getChatView() {
        return "";
    }
}
