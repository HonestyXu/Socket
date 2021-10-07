import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static Socket socket;
    public static boolean connect_status = false;
    public static void main(String[] args){
        connect();
        if (connect_status){
            new Thread(new Client_Listen(socket)).start();
            new Thread(new Client_Send(socket)).start();
        }
    }

    public static void connect(){
        try{
            socket = new Socket("127.0.0.1", 9999);
            connect_status = true;
        }catch (Exception e){
            connect_status = false;
            e.printStackTrace();
        }
    }

}

class Client_Listen implements Runnable{
    private Socket socket;

    public Client_Listen(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while(true)
                System.out.println(ois.readObject());
        }catch (Exception e){
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

class Client_Send implements Runnable{
    private Socket socket;

    public Client_Send(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.print("Please input client msg: ");
                String msg = scanner.nextLine();
                oos.writeObject(msg);
                oos.flush();
            }
        }catch (Exception e){
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