package Requests;

import java.io.*;

public class Request {
    private BufferedWriter bw;
    private BufferedReader br;

    private static final int MAX_READ = 1024;
    private static final int OFFSET = 0;


    public Request(BufferedWriter bw, BufferedReader br) {
        this.bw = bw;
        this.br = br;
    }

    public void transferRequest() throws IOException {
            int num_read = 0;
            char [] string_read = new char [MAX_READ];

            num_read = br.read(string_read,OFFSET,MAX_READ);

            while(0 < num_read){
                bw.write(string_read,OFFSET,num_read);
                bw.flush();
                num_read = br.read(string_read,OFFSET,MAX_READ);
            }
    }
}
