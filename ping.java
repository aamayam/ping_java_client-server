
package Client;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Alan Amaya 191165 (aamayama@itam.mx)
 */
public class ping {
    
    public static void main(String[] args) {
        
        Scanner readS = new Scanner(System.in);        
        
        System.out.println("===== WELCOME TO PING PROGRAM =====");
        System.out.println("FIRST, SET THE NEXT FIELDS IN ORDER TO" +
                " CONFIGURE PING");
        System.out.println("IP ADDRESS FOR CLIENT");
        System.out.print(">>>");
        String ipLocal = readS.nextLine();
        
        System.out.println("Structure of normalized sentence for ping:");
        System.out.println("ping <host> [port] [message] "
                + "[number of requests]");
        
        boolean cond;
        do{           
            
            System.out.print(">>>");

            String inputSentence = readS.nextLine();
            String sentence[] = inputSentence.split(" ");
            cond = checkInput(sentence);
            if(cond){
                String dir = sentence[1];                
                String msg = sentence[3];
                msg = msg+"00";
                try{
                    int port = Integer.parseInt(sentence[2]);
                    int nr = Integer.parseInt(sentence[4]);
                    
                    //  Initial value of current thread 
                    long st = System.currentTimeMillis();
                    
                    //  Array of reply times
                    long[] tms = new long[nr];
                    
                    //  Array of succes or failed packets
                    int replies = 0;
                    int errors = 0;
                    
                    for(int i = 1;i<=nr;i++){
                        //  Structure of buffer String
                        //  type|seq|payload
                        //  type: 0 - request; 1 - reply; 2 - error
                               
                        String buffer = "0"+i+msg;
                        
                        //  Writing
                        DatagramSocket socket = new DatagramSocket(port,
                                InetAddress.getByName(ipLocal));
                        
                        socket.setSoTimeout(1000);

                        DatagramPacket sendPacket = new DatagramPacket(
                                buffer.getBytes(),msg.length(),
                                InetAddress.getByName(dir),port);

                        socket.send(sendPacket);
                        long startTime = System.currentTimeMillis();

                        //  Reading
                        byte[] rep = new byte[1024];
                        DatagramPacket repPack = new DatagramPacket(rep,
                                rep.length);

                        try{
                            socket.receive(repPack);
                            long endTime = System.currentTimeMillis();
                            long time = endTime - startTime;
                            tms[i-1] = time;

                            String data = new String(repPack.getData());
                            //String d[] = data.split("\\|")

                            System.out.println("reply from " +
                                    repPack.getAddress().getHostAddress() +
                                    " by port " + repPack.getPort() +
                                    "; says: " + data.substring(2,data.length()) +
                                    " udp_seq="+i + " time=" + 
                                    time + " ms");
                            
                            //  Increasing replies counter
                            replies++;                            
                        }catch(SocketTimeoutException e){
                            System.out.println("From " + 
                                    dir + " icmp_seq " + i +
                                    " Destination Host Unreachable");
                            
                            //  Increasing errors counter
                            errors++;
                        }
                        

                        socket.close();
                        
                        cond = false;
                        TimeUnit.MILLISECONDS.sleep(600);
                    }
                    
                    long et = System.currentTimeMillis();
                    long finalTime = et - st - (nr*600);
                    
                    System.out.println("\n- - - - Statistics for " + dir +
                            " ping - - - -");
                    System.out.println(nr + " packets transmitted," + 
                            replies + " received," + errors
                            + " packet loss, time " + 
                            finalTime + " ms");
                    
                    double avg = Math.round(
                            Arrays.stream(tms).average().getAsDouble() 
                                    * 100)/100;
                    System.out.println("RTT min/avg/max = " + 
                            Arrays.stream(tms).min().getAsLong() + 
                            "/" +  avg + 
                            "/" + Arrays.stream(tms).max().getAsLong());
                    
                    
                }catch(SocketException ex){
                    System.out.println("error");
                }catch(InterruptedException ex){
                    System.out.println("erer");
                }
                catch (IOException e){
                    System.out.println(e);
                    cond = false;
                }catch (NumberFormatException e){
                    System.out.println("Invalid integer input");
                    cond = false;
                }
            }else{                
                if(sentence.length==1 && (sentence[0].equals("quit") 
                        || sentence[0].equals("QUIT"))){ cond = true;}
                else if(sentence[3].length() > 40){
                    System.out.println("Input size exceded");
                }
                else{System.out.println("Invalid input");}
            }            
        }while(!cond);       
               
        readS.close();
    }
    
    private static boolean checkInput(String[] s){
        boolean res = true;
        if(s.length!= 5){res = false;}
        else if(s[0].equals("ping") && s[0].equals("PING")) {res = false;}
                      
        if(s[3].length() > 40) {res = false;}
            
        return res;
    }
    
}
