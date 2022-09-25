package Thread;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Alan Amaya 191165 (aamayama@itam.mx)
 */
public class ThreadHandler extends Thread{
    
    private DatagramSocket socket;
    
    public ThreadHandler(DatagramSocket s){
        socket = s;
    }
    
    @Override
    public void run(){
        try{
            byte[] buffer = new byte[1024];
            DatagramPacket clientReq = new DatagramPacket(buffer,1024);
            
            boolean cond;
            
            do{                                
                //  Reading
                socket.receive(clientReq);
                String buf = new String(clientReq.getData());
                
                //  Getting header and payload
                //  Getting paylod
                String msg = buf.subSequence(2,buf.length()).toString();
                //  Getting number of sequence 
                int seq = Integer.parseInt(buf.charAt(1)+"");

                System.out.println("request from " 
                        + clientReq.getAddress().getHostAddress()
                        + " by port " + clientReq.getPort()
                        + " message: " + msg
                        );
                
                if(!msg.equals("exit0")){                    
                    //  Writing
                    Date date = new Date();
                    String dateStr = new SimpleDateFormat(
                            "dd-MM-yyyy HH:mm:ss").format(date);
                    String repMsg = "Hi! its " + dateStr;
                    
                    //  Creating send header and payload
                    
                    String sbuf = "2"+seq+repMsg;
                    
                    //  Creating reply datagram
                    InetAddress addr = clientReq.getAddress();
                    int port = clientReq.getPort();

                    DatagramPacket sendPacket = new DatagramPacket(
                            sbuf.getBytes(),repMsg.length(),
                            addr,
                            port);
                    socket.send(sendPacket);
                    cond = true;
                }else {
                    cond = false;
                }                   
                                
            }while(cond);
            
            socket.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
}
