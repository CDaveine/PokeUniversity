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

    private int size_games(Game[] games){
        int i = 0;
        while(games[i] != null){
            i++;
        }
        return i;
    }

    private String message_game(Game[] games){
        String s = "number of games " + size_games(games) + "\n";
        for(int i = 0; i < size_games(games); i++){
            s += games[i].display();
        }
        return s;
    }

    private boolean creation_game(String read){
        String titre = read.substring(11);
        Game game = new Game(1, titre);
        if(size_games(games) < 4){
            games[size_games(games)] = game;
            return true;
        }else{
            return false;
        }
    }

    private boolean join_game(String read){
        String titre = read.substring(10);
        for(int i = 0; i < size_games(games); i++){
            if(games[i].getGame_name().equals(titre)){
                if(games[i].getNb_player() < 4){
                    return true;
                }else{
                    System.out.println("nb joueur max atteint");
                }
            }else{
                System.out.println("cette partie n'existe pas");
            }
        } return false;
    }

	
	public void run(){
        while(true){
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            while ((read = in.readLine())!= null) {
                System.out.println("message recu : " + read);
                if(read.contains("require game list")){
                    games[0]=game1;
                    out.println(message_game(games));
                } else if(read.contains("create game")){
                    System.out.println("ok");
                    if(creation_game(read) == true){
                        System.out.println("ici");
                        creation_game(read);
                        System.out.println("la");
                        out.println("game created\n");
                        System.out.println("euh");
                    }else{
                        out.println("cannot create game\n");
                    }
                }else if (read.contains("join game")){
                    if(join_game(read)){
                        System.out.println("ok");
                        out.println("game joined\n");
                    }else{
                        out.println("cannot join game\n");
                    }
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
    }}
}

}