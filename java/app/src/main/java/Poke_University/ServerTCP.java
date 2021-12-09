package Poke_University;

import java.io.*;
import java.net.*;
  
// Server class
class ServerTCP implements Closeable, Runnable{
	private final static int PORT = 9001;
	public ServerSocket socket;
    Socket client;
    Game[] games = new Game[4];
    Game game1 = new Game(2, "bob");

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

    private int taille_games(Game[] games){
        int i = 0;
        while(games[i] != null){
            i++;
        }
        return i;
    }

    private String message_game(Game[] games){
        String s = "number of games " + taille_games(games) + "\n";
        for(int i = 0; i < taille_games(games); i++){
            s += games[i].display();
        }
        return s;
    }
	
	public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((read = in.readLine()) != null) {
                if(read.contains("require game list")){
                    out = new PrintWriter(client.getOutputStream(), true);
                    games[0]=game1;
                    out.println(message_game(games));
                }
            }
        }
    	catch (IOException e) {
            e.printStackTrace();
        }
        finally {
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
        }
    }
}

}