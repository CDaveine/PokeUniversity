package Poke_University;

import java.io.*;
import java.net.*;
  
// Server class
class ServerTCP implements Closeable, Runnable{
	private final static int PORT = 9001;
	public ServerSocket socket;
    Socket client;
    Server serv = new Server();


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
                }catch(IOException e){
                    e.printStackTrace();
                }

            }
    }


					   
 // ClientHandler class
public class ClientHandler implements Runnable {
	Socket clientSocket;	
    String read;
    PrintWriter out;
    BufferedReader in;
		   
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
        while(true){
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            while ((read = in.readLine())!= null) {
                System.out.println("message recu : " + read);
                if(read.contains("require game list")){
                    System.out.print(serv.message_game());
                    out.println(serv.message_game());
                } else if(read.contains("create game")){
                    System.out.println("ok");
                    if(serv.creation_game(read) == true){
                        System.out.println("ici");
                        System.out.println("la");
                        out.println("game created");
                        System.out.println("game created");
                    }else{
                        out.println("cannot create game");
                    }
                }else if (read.contains("join game")){
                    if(serv.join_game(read)){
                        System.out.println("ok");
                        out.println("game joined");
                    }else{
                        out.println("cannot join game");
                    }
                }
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
                    client.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }}
}

}