/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_WRITE;

// multipleksowania kanałów gniazd (użycie selektora),
// serwer może obsługiwać równolegle wielu klientów, ale obsługa żądań klientów odbywa się w jednym wątku,
public class ChatServer extends Thread {
    private String host;
    private int port;
    private ServerSocketChannel server;
    private Selector selector;
    private SelectionKey selectionKey;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
        server = null;
    }

    // uruchamia serwer w odrębnym wątku
    public void startServer() throws IOException {
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(host, port));
        server.configureBlocking(false);
        selector = Selector.open();
        selectionKey = server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started");
        start();
    }

    // zatrzymuje serwer i wątek, w którym działa
    public void stopServer() throws IOException {
        interrupt();
        server.close();
        selector.close();
        System.out.println("Server stopped");
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while(iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ | OP_WRITE);
                        continue;
                    }
                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        continue;
                    }
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        continue;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // zwraca log serwera
    public String getServerLog() {
        return "=== Server log ===";
    }
}
