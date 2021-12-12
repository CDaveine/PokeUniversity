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
}