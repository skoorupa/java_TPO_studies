/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.channels.SelectionKey.OP_WRITE;

// multipleksowania kanałów gniazd (użycie selektora),
// serwer może obsługiwać równolegle wielu klientów, ale obsługa żądań klientów odbywa się w jednym wątku,
public class ChatServer extends Thread {
    private Lock lock = new ReentrantLock();
    private StringBuffer log;
    private String host;
    private int port;
    private ServerSocketChannel server;
    private Selector selector;
    private SelectionKey selectionKey;
    private Map<SocketChannel, String> allClients;
    //
    private static Charset charset = StandardCharsets.UTF_8;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.server = null;
        this.allClients = new HashMap<>();
        this.log = new StringBuffer();
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
        lock.lock();
        interrupt();
        selector.wakeup();
        selector.close();
        server.close();
        System.out.println("Server stopped");
        lock.unlock();
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                selector.select();

                if (isInterrupted())
                    break;

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while(iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        continue;
                    }
                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        serviceRequest(client);
                        continue;
                    }
//                    if (key.isWritable()) {
//                        SocketChannel client = (SocketChannel) key.channel();
//                        continue;
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void serviceRequest(SocketChannel client) throws IOException {
        if (!client.isOpen()) return;

        Matcher matcher = Pattern.compile("([^#]+)#(.+)").matcher(readMessage(client));
        if (!matcher.find()) return;
        CallMethod callMethod = CallMethod.valueOf(matcher.group(1));
        String msg = matcher.group(2);

        if (callMethod == CallMethod.LOGIN)
            allClients.put(client, msg);

        String id = allClients.get(client);
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String response = "";

        switch (callMethod) {
            case LOGIN:
                response = id+" logged in";
                break;
            case LOGOUT:
                response = id+" logged out";
                allClients.remove(client);
                client.close();
                break;
            case MESSAGE:
                response = id+": "+msg;
                break;
        }

        addLog(timestamp+" "+response);
        for (SocketChannel socket : allClients.keySet()) {
//            if (socket.isOpen())
                socket.write(charset.encode(response + '\n'));
        }
    }

    private String readMessage(SocketChannel socket) throws IOException {
        ByteBuffer inBuf = ByteBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();

        while (socket.read(inBuf) > 0) {
            inBuf.flip();
            CharBuffer charBuffer = charset.decode(inBuf);
            while (charBuffer.hasRemaining()) {
                char c = charBuffer.get();

                if (c != '\n')
                    sb.append(c);
                else
                    return sb.toString();
            }
            inBuf.clear();
        }

        return "";
    }

    private void logException(Exception e) {
        addLog("*** "+e.toString());
    }

    private void addLog(String msg) {
        log.append(msg).append("\n");
    }

    // zwraca log serwera
    public String getServerLog() {
        return "\n"+log.toString();
    }
}
