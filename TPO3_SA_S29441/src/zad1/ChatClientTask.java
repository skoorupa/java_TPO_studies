/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;

import java.util.List;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask {
    private ChatClient chatClient;
    private List<String> msgs;
    private int wait;

    public ChatClientTask(ChatClient c, List<String> msgs, int wait) {
        super();
        this.chatClient = c;
        this.msgs = msgs;
        this.wait = wait;
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(c, msgs, wait);
    }

    public ChatClient getClient() {
        return chatClient;
    }
}
