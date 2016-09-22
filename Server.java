import java.io.IOException; // needed for throwing the IOException
import java.io.PrintStream; //needed for printstream which sends info back to client
import java.net.ServerSocket; // needed to create the server socket
import java.net.Socket; // needed to create socket that is used to accept connection
import java.util.Scanner; // needed to scan in input from client

public class Server 
{
    public static void main(String args[]) throws IOException
    {
        String message, temp;
        
        ServerSocket s1 = new ServerSocket(1342); //creates socket that listens to port 1342 
        Socket sock = s1.accept(); //accepts a connection from client
        Scanner scan = new Scanner(sock.getInputStream()); // gets input from connection with client
        
        message = scan.nextLine(); //assigns input from client to variable for modification 
        temp = message.toUpperCase(); //modifies user input to be in all caps
        
        PrintStream p = new PrintStream(sock.getOutputStream()); // creats print stream that will send modified input backk to client
        p.println(temp); //prints out modified input back to client
    }
}
