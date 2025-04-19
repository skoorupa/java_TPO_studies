/**
 *
 *  @author Skorupski Adam S29441
 *
 */

package zad1;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask<ChatClient> {
    public ChatClientTask(Callable<ChatClient> call) {
        super(call);
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(()->sendMsgs(c, msgs, wait));
    }

    private static ChatClient sendMsgs(ChatClient c, List<String> msgs, int wait) {
        try {
            c.login();
            if (wait != 0) Thread.sleep(wait);
            for (String msg : msgs) {
                c.send(msg);
                if (wait != 0) Thread.sleep(wait);
            }
            c.logout();
            if (wait != 0) Thread.sleep(wait);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public ChatClient getClient() {
        try {
            return this.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
