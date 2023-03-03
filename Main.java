import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Solution {
    /**
     * We create a separate thread and delegate it with the functionality of checking if the text matches the regex.
     * Since this can take a considerable amount of time for a very long text and a complex regex, we need to use a
     * separate thread. We use the .join(long millis) method of the thread class to effectively tell our main thread
     * "Wait 5 seconds for this thread to complete". After that, we check if the regexThread is still alive, and if it
     * is, it means the regex matcher takes too long to complete, so we throw a TimeoutException.
     */
    public boolean matches(String text, String regex) throws InterruptedException, TimeoutException {
        AtomicBoolean result = new AtomicBoolean(false);
        Matcher matcher = Pattern.compile(regex).matcher(text);

        Thread regexThread = new Thread(() -> result.set(matcher.matches()));

        regexThread.start();
        regexThread.join(5000);

        if (regexThread.isAlive()) {
            regexThread.interrupt();
            throw new TimeoutException("Regex expression took more than 5 seconds to complete");
        }

        return result.get();
    }
}

public class Main {
    public static void main(String[] args) { }
}
