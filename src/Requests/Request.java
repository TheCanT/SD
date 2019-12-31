package Requests;

import java.io.*;

public class Request {
    private DataOutputStream bw;
    private DataInputStream br;

    private static final int MAX_READ = 1024;
    private static final int OFFSET = 0;


    public Request(DataOutputStream bw, DataInputStream br) {
        this.bw = bw;
        this.br = br;
    }

    public void transferRequest() throws IOException {
            int num_read = 0;
            byte [] byte_read = new byte [MAX_READ];

            num_read = br.read(byte_read,OFFSET,MAX_READ);

            while(0 < num_read){
                bw.write(byte_read,OFFSET,num_read);
                bw.flush();
                num_read = br.read(byte_read,OFFSET,MAX_READ);
            }
    }
}
