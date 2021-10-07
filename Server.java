import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while(true) {
                Socket socket = serverSocket.accept();
                System.out.print("New connection");
                new Thread(new Server_Listen(socket)).start();
                new Thread(new Server_Send(socket)).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Server_Listen implements Runnable{
    private Socket socket;

    public Server_Listen(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println(ois.readObject());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

class Server_Send implements Runnable{
    private Socket socket;

    public Server_Send(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.print("Please input msg: ");
                String msg = scanner.nextLine();
                oos.writeObject(msg);
                oos.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
