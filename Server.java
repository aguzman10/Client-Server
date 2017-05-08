import java.io.IOException;
import java.util.*;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/*
 * @author Alejandro Guzman
 * Software Engineering Major - UTD
 *
 * Client-Server program:
 *
 * Server side of a client-server program that implements AES encryption
 *
 */

public class Server
{
	static ArrayList<String> userNames = new ArrayList<String>();
	static ArrayList<String> secretKeys = new ArrayList<String>();

	private static SecretKeySpec secretKey;
	private static byte[] key;
	public static void main(String[] args) throws IOException
	{
		String message="", temp, userName = "";

		String[] tokens;
		int index = -1;
		boolean run = true;

		System.out.println("\nReading and registering client IDs and secret keys...");
		
		try
		{
			Scanner input = new Scanner(new java.io.File("subscriptions.txt"));
			String str;

			while(input.hasNext())
			{
				str = input.nextLine();
				tokens = str.split(" ");
				userNames.add(tokens[0]);
				secretKeys.add(tokens[1]);
			}
			input.close();
		}
		catch (java.io.FileNotFoundException e)
		{
			e.printStackTrace();
		}

		System.out.println("\nWaiting for client to connect to server...");

		ServerSocket s1 = new ServerSocket(1342);
		Socket sock = s1.accept();
		Scanner scan = new Scanner(sock.getInputStream());

		userName = scan.nextLine();
	
		for(int i = 0; i < userNames.size(); i++)
		{
			if(userName.equals(userNames.get(i)))
			{
				index = i;
				System.out.println("\nClient " + userName + " has connected to the server!");
			}
		}

		setKey(secretKeys.get(index));
		while(run)
		{
			try
			{
				message = scan.nextLine();
			}
			catch (NoSuchElementException e)
			{
				System.out.println("\nClient has disconnected from server...");
				run = false;
				break;
			}
			////
			System.out.println("\nEncrypted message received from client: " + message);
			////
			message = decrypt(message).toString();

			System.out.println("\nDecrypted message received from client: " + message);

			temp = message.toUpperCase();
			/////
			System.out.println("\nDecrypted message after being uppercased in server class: " + temp);
			////
			temp = encrypt(temp).toString();
			////
			System.out.println("\nEncrypted message after being uppercased in server class: " + temp);
			System.out.println();
			////
			PrintStream p = new PrintStream(sock.getOutputStream());
			p.println(temp);
		}
	}

	public static void setKey(String myKey)
	{
		MessageDigest sha = null;
		try
		{
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key,16);
			secretKey = new SecretKeySpec(key, "AES");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	public static String encrypt(String strToEncrypt)
	{
		//byte[] encrypted = null;

		try
		{
			System.out.println("\nNow encrypting message...");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			//encrypted = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			System.out.println("\nError while encrypting: " + e.toString());
		}

		return null;
	}

	public static String decrypt(String encrypted)
	{
		//byte[] decrypted = null;
		
		try
		{
			System.out.println("\nNow decrypting message...");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			//decrypted = cipher.doFinal(d);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
		}
		catch (Exception e)
		{
			System.out.println("\nError while decrypting: " + e.toString());
		}

		return null;
	}
}
