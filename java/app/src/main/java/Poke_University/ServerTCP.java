package Poke_University;

import java.io.*;
import java.net.*;
  
// Server class
class ServerTCP implements Closeable, Runnable{
	private final static int PORT = 9001;
	public ServerSocket socket;
    Socket client;

    public ServerTCP() {
		try {
			socket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket accept() throws IOException{
		return socket.accept();
	}

	@Override
	public void close() throws IOException{
		socket.close();
	}

    @Override
    public void run(){
            // server is listening on port 1234
            // running infinite loop for getting
            // client request
        //while (true) {
                // socket object to receive incoming client
                // requests
            while(true){
                try{
                    client = socket.accept();
                    // Displaying that new client is connected
                    // to server
                //System.out.println("New client connected " + client.getInetAddress().getHostAddress());
                    // create a new thread object
                ClientHandler clientSock = new ClientHandler();                // This thread will handle the client
                    // separately
                new Thread(clientSock).start();
                System.out.println("la");
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            
			/*if (socket != null) {
				try {
					socket.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}*/
		//}
    }


					   
 // ClientHandler class
public class ClientHandler implements Runnable {
	Socket clientSocket;	
    String read;
		   
	// Constructor
	public ClientHandler(){
		try {
            //client = socket.accept();
            System.out.println("Client Got connected ");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void run(){
        try {
            System.out.println("hello");

            // get the outputstream of client
            // get the inputstream of client
            System.out.println("couccou");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("je suis la");
            while ((read = in.readLine()) != null) {
                if(read.contains("require game list")){
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println("number of games 1\n bob bob\n");
                }
            // writing the received message from
            // client
            //System.out.printf(" Sent from the client: %s\n", read);
			//out.println(line);
            }
        }
    	catch (IOException e) {
            e.printStackTrace();
        }
        /*finally {
			try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }
}

}