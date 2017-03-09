package day10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 服务器端
 */
public class SocketClientB {
	Socket s = null;
	public SocketClientB(String address) {
		try {
			s = new Socket(address, 8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String readMessage() throws Exception{
		BufferedReader br = new BufferedReader(
				new InputStreamReader(s.getInputStream()));
		String message = br.readLine();
		return message;
	}
	public void sendMessage(String message) throws Exception {
		PrintWriter pw =  new PrintWriter(s.getOutputStream());
		pw.println(message);
		pw.flush();
	}
	public static void main(String[] args) throws Exception {
		Scanner console = new Scanner(System.in);
		String address = null;
		String name = null;
		while (true) {// 通过客户端给限制
			System.out.println("请输入主机IP：");
			// 169.254.113.184
			address = console.nextLine();
			if(address.equals("")||address.equals("\n")) {
				continue;
			}
			// 输入名字
			System.out.println("请输入用户名：");
			name = console.nextLine();
			if(name.equals("")||name.equals("\n")) {
				continue;
			}
			break;
		}
		final SocketClientB s = new SocketClientB("127.0.0.1");
		/*
		 *  持续接收从服务器返回的信息
		 *  且独立   
		 *  实现客户端的多线程
		 */
		new Thread() {
			public void run() {
				while (true) {
					String mess;
					try {
						mess = s.readMessage();
						System.out.println(mess);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		/*
		 *  从控制台输入聊天信息给服务器
		 *  不间断读取控制台信息
		 */
		while (true) {
			String message = console.nextLine();
			s.sendMessage(name +":"+ message);
		}
		
		
	}
}






