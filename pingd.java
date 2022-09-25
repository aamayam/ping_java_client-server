package Server;

//import Hilos.ThreadedEchoHandler;
import Thread.ThreadHandler;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Alan Amaya 191165 (aamayama@itam.mx)
 */
public class pingd {
    
    public static void main(String[] args) {
        int count = 1;
        Scanner rd = new Scanner(System.in);
        
        System.out.println("===== WELCOME TO PINGD =====");
        System.out.println("INITIALIZING MAIN CONFIGURATION FOR SERVER:");
        System.out.println("Set the next fields to initialize server:");
        System.out.println("SERVER UP [ip address] [port]");
        
        //  Checking inputs
        
        boolean cond;
        do{
            System.out.print(">>>");

            String inputSentence = rd.nextLine();
            String sentence[] = inputSentence.split(" ");
            cond = checkString(sentence);
            if(cond){
                String dir = sentence[2];               

                try{
                    int port = Integer.parseInt(sentence[3]);
                    while (count < 6){
                        DatagramSocket socket = new DatagramSocket(port,
                                InetAddress.getByName(dir));
                        System.out.println("SERVER SUCCESSFULLY UP");
                        System.out.println("Waiting for a connection by port " + 
                                port + "...");
                        ThreadHandler th = new ThreadHandler(socket);
                        th.start();

                        while(th.isAlive()){ }                
                        count++;
                    }
                }catch (IOException e) { 
                    System.out.println("FAILURE IN SERVER ACTIVATION ATTEMP");
                    System.out.println(e); 
                    cond = false;
                }catch (NumberFormatException e){
                    System.out.println("invalid port value");
                    cond = false;
                }
            }else {
                System.out.println("invalid input");
            }            
        }while(!cond);
        
        rd.close();
    }
    
    private static boolean checkString(String[] s){
        boolean res = true;
        if(s.length != 4){
            res = false;
        }else if(!s[0].equals("server") && !s[0].equals("SERVER")){
            res = false;
            System.out.println("^");
        }else if(!s[1].equals("up") && !s[1].equals("UP")){
            res = false;
            System.out.println("^^");
        }
        
        return res;
    }
    
}
