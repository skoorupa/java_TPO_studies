/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;

import java.nio.channels.ServerSocketChannel;

// multipleksowania kanałów gniazd (użycie selektora),
// serwer może obsługiwać równolegle wielu klientów, ale obsługa żądań klientów odbywa się w jednym wątku,
public class ChatServer {
    private String host;
    private int port;
    private ServerSocketChannel server;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
        server = null;
    }

    // uruchamia serwer w odrębnym wątku
    public void startServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // zatrzymuje serwer i wątek, w którym działa
    public void stopServer() {
    }

    // zwraca log serwera
    public String getServerLog() {
        return "";
    }
}
