
package Poke_University;

import Poke_University.*;
import java.io.*;
import java.net.*;

public class App{

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        try(ServerUDP serverUDP = new ServerUDP()){
            //try (ServerTCP serverTCP = new ServerTCP()) {
                Thread threadUDP = new Thread(serverUDP);
                threadUDP.start();
               // new Thread(serverTCP).start();
            /*} catch (IOException e) {
                e.printStackTrace();
            }*/

        }
        //udp
        /*try (ServerUDP s = new ServerUDP()) {
            s.run();;
		}
        try (ServerTCP serv = new ServerTCP()) {
            Socket client = serv.accept();

            //BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

            //String str = reader.readLine();

            //System.out.println(str);

            writer.println("number of games 0");

        } catch (IOException e) {
            e.printStackTrace();
        }

        
        Attacks att = new Attacks();
        Alabourre ala = new Alabourre();
        Alabourre alab = new Alabourre();
        Enseignant_dresseur ens = new Enseignant_dresseur();
        System.out.println(ala.rand_attack(2));
        ala.display();
        alab.display();
        ens.display();
        ens.xp = 555;
        ens.attack(alab, ens.attacks[0]);
        ens.experience(alab, 2);
        ala.display();
        alab.display();
        ens.display();*/
    }
}
