import java.io.IOException;
import java.util.*;
import java.io.PrintStream;
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
 *Client-Server program:
 *
 * Client side of a client-server program that implements AES encryption
 *
 */

public class Client
{
	static ArrayList<String> userNames = new ArrayList<String>();
	static ArrayList<String> secretKeys = new ArrayList<String>();
	private static SecretKeySpec secretKey;
	private static byte[] key;
	
	public static void main(String[] args) throws IOException
	{
		String message, temp, userName="";
		boolean user = true, run = true;

		Scanner scan = new Scanner(System.in);
		Socket s = new Socket("127.0.0.1", 1342);
		Scanner scan1 = new Scanner(s.getInputStream());
		String[] tokens;
		int index = -1;

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

		
		while(user)
		{
			System.out.println("Please enter your user name: ");
			userName = scan.nextLine();
			for(int i = 0; i < userNames.size(); i++)
			{
				if(userName.equals(userNames.get(i)))
				{
					index = i;
					user = false;
					break;
				}
			}
			if(index == -1)
			{
				System.out.println("Sorry that user is not found, please check the user name entered for errors");
			}
		}

		PrintStream p = new PrintStream(s.getOutputStream());
		p.println(userName);
		while(run)
		{
			System.out.println("Enter the message you want to send to the server(type 'quit' to stop): ");
			message = scan.nextLine();
			if(message.equals("quit"))
			{
				run = false;
				break;
			}
			message = encrypt(message, secretKeys.get(index)).toString();
		
			p.println(message);

			temp = scan1.nextLine();
		
			temp = decrypt(temp).toString();
		
			System.out.println(temp);
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
			key = Arrays.copyOf(key, 16);
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

	public static String encrypt(String strToEncrypt, String secret)
	{
		//byte[] encrypted = null;
		try
		{
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		//	encrypted = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			System.out.println("Error while encrypting: " + e.toString());
		}

		return null;
	}

	public static String decrypt(String encrypted)
	{
		//byte[] decrypted = null;
	
		try
		{
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
		}
		catch (Exception e)
		{
			System.out.println("Error while decrypting: " + e.toString());
		}

		return null;
	}
}