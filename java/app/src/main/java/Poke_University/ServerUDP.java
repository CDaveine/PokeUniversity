package Poke_University;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerUDP implements Communicate, Runnable, Closeable{
	
	private final static int PORT = 9000;

	private static final String SEARCH_SERVER = "looking for poketudiant servers\n";
	private static final String ANSWER_SEARCH_SERVER = "i'm a poketudiant server\n";
	private static final String ANSWER_ERROR = "I don't understand your request\n";

	private DatagramSocket socket;
	private InetSocketAddress addr;
	private byte[] receiveData = new byte[1024];

	/**
	 * Create a new server using UDP
	 */
	public ServerUDP() {
		try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e) {

			if (!socket.isClosed()) {
				System.err.println(socket.getLocalPort());
				System.err.println(socket.getPort());
				socket.close();
			}
			e.printStackTrace();
		}
	}

	@Override
	public String receive() {
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			socket.receive(receivePacket);
			addr = new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort());
			return new String(receivePacket.getData(),0,SEARCH_SERVER.length());
		} catch (IOException e) {
			e.printStackTrace();
			if (!socket.isClosed()) {
				socket.close();
			}
		}
		return null;

	}

	@Override
	public void send(String msg) {
		try {
			byte[] sendData = msg.getBytes();
			DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, addr.getAddress(),
					addr.getPort());
			socket.send(datagramPacket);
		} catch (IOException e) {
			if (!socket.isClosed()) {
				socket.close();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		socket.close();
	}


	@Override
	public void run() {
        try(ServerUDP s = new ServerUDP()){
            while (true) {
                String msg = s.receive();
                System.out.println("Message reçu:" + msg);
                if (msg.contains(SEARCH_SERVER)) {
                    System.out.println(ANSWER_SEARCH_SERVER);
                    s.send(ANSWER_SEARCH_SERVER);
                } else {
                    System.out.println(ANSWER_ERROR);
                    s.send(ANSWER_ERROR);
                }
            }
        }
    }


































    /*TCP threads

	private final static int PORT = 9001;
	private static ServerSocket server;

	public Socket accept() throws IOException{
		return server.accept();
	}

	@Override
	public void close() throws IOException{
		server.close();
	}
    public static void main(String[] args){
        try {
            // server is listening on port 1234
            server = new ServerSocket(PORT);
            // running infinite loop for getting
            // client request
            while (true) {
                // socket object to receive incoming client
                // requests
                Socket client = server.accept();
                // Displaying that new client is connected
                // to server
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());
				// create a new thread object
				ClientHandler clientSock = new ClientHandler(client);
				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

					   
 // ClientHandler class
private static class ClientHandler implements Runnable {
	private final Socket clientSocket;			   
	// Constructor
	public ClientHandler(Socket socket){
		this.clientSocket = socket;
	}
	
	public void run(){
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            // get the outputstream of client
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            // get the inputstream of client
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            // writing the received message from
            // client
            System.out.printf(" Sent from the client: %s\n", line);
			out.println(line);
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
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
*/

}