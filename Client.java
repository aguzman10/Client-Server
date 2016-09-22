import java.io.IOException; // needed for throwing the IOException
import java.io.PrintStream; //needed for printstream which sends info back to client
import java.net.Socket; // needed to create socket that is used to accept connection
import java.util.Scanner; // needed to scan in input from client

public class Client
{
    public static void main(String[] args) throws IOException
    {
        String message, temp;
        
        Scanner scan = new Scanner(System.in); //scanner used to get user input message
        Socket s = new Socket("127.0.0.1",1342); //creates socket using the local host, and port 1342
        Scanner scan1 = new Scanner(s.getInputStream()); //scanner that is used to get input from server
        System.out.println("Enter the message you wish to set to uppercase: "); // message that prompts user for their input
        message = scan.nextLine(); //scanner gets user input and assigns it to String message
        PrintStream p = new PrintStream(s.getOutputStream()); //printstream that is created to send user input over to the server
        p.println(message); //prints out user message to the server
        
        temp = scan1.nextLine(); //scanner scan1 picks up the output from the server print stream that is the modified  message from the user
        System.out.println(temp); //prints out the modified message from the server for the user to see
    }
}
