import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente implements Runnable {

    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost",35353);
        PrintWriter pw = new PrintWriter(s.getOutputStream(), true);

        pw.write("ola servidor daqui user");
        pw.flush();
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("asdasd");
    }

    @Override
    public void run() {

    }
}
