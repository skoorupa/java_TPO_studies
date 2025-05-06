/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatClient extends Thread {
    private Lock lock = new ReentrantLock();
    private StringBuffer log;
    private String host;
    private int port;
    private String id;
    private SocketChannel socket;
    //
    private static Charset charset = StandardCharsets.UTF_8;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.log = new StringBuffer();

        addLog("=== "+id+" chat view");
    }

    public void login() {
        try {
            socket = SocketChannel.open();
            socket.configureBlocking(false);
            socket.connect(new InetSocketAddress(host, port));
            while (!socket.finishConnect()) {
                Thread.sleep(10);
            }
            start();
            callServer(CallMethod.LOGIN,id);
        } catch (Exception e) {
            logException(e);
        }
    }

    public void logout() {
        try {
            callServer(CallMethod.LOGOUT,id);
            addLog(id+" logged out");
            lock.lock();
            interrupt();
        } catch (Exception e) {
            logException(e);
        } finally {
            lock.unlock();
        }
    }

    public void send(String req) {
        callServer(CallMethod.MESSAGE, req);
    }

    private void callServer(CallMethod callMethod, String msg) {
        try {
            socket.write(charset.encode("#"+callMethod.toString()+"#"+msg+'\n'));
        } catch (Exception e) {
            logException(e);
        }
    }

    public void run() {
        ByteBuffer inBuf = ByteBuffer.allocateDirect(1024);
        StringBuffer sb = new StringBuffer();

        int readBytes = 0;
        while (!isInterrupted()) {
            try {
                lock.lock();
                readBytes = socket.read(inBuf);
                if (readBytes > 0) {
                    inBuf.flip();
                    CharBuffer cb = charset.decode(inBuf);
                    while (cb.hasRemaining()) {
                        char c = cb.get();
                        if (c != '\n' || cb.hasRemaining())
                            sb.append(c);
                    }
                } else if (readBytes == -1)
                    break;
            } catch (AsynchronousCloseException e) {
                break;
            } catch (Exception e) {
                logException(e);
            } finally {
                lock.unlock();
            }

            String response = sb.toString();
            if (!response.trim().isEmpty()) {
                addLog(response);
                sb.setLength(0);
            }

            inBuf.clear();
        }
    }

    public String getChatView() {
        return log.toString();
    }

    private void logException(Exception e) {
        addLog("*** "+e.toString());
    }

    private void addLog(String msg) {
        log.append(msg+"\n");
    }
}
