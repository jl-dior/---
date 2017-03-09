package day10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
/**
 * 服务器端
 */
public class SocketServerB {
	// 定义服务
	ServerSocket ss = null;
	// 定义集合，存放所有连接
	HashSet<Socket> allSockets = new HashSet<Socket>();
	public SocketServerB() {
		try {
			// 初始化服务器
			ss = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 自己定义一个方法，用来接收连接
	 */
	public void startService() {
		int count = 0;
		while (true) {// 服务开启后，一直接收连接，所以是死循环
			try {
				Socket s = ss.accept();
				count ++;
				allSockets.add(s);// 把获得的连接存入集合中
//				System.out.println(count);
				// 实现服务器端的多线程
				new ServerThread(s).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 自定义方法，用来向客户端发送信息
	 * 同一条信息，发向所有客户端
	 * 客户端已知(属性)
	 * 信息已知(参数)
	 */
	private void sendMessageToClient(String message) throws Exception {
		for (Socket socket : allSockets) {
			PrintWriter pw = new PrintWriter(
					socket.getOutputStream());
			pw.println(message);
			pw.flush();
		}
	}
	
	public static void main(String[] args) {
		new SocketServerB().startService();
	}
	private class ServerThread extends Thread {
		Socket s ;
		public ServerThread(Socket s) {
			this.s = s;
		}
		public void run() {
			BufferedReader br = null;
			try {
				while (true) {
					br = new BufferedReader(
							new InputStreamReader(
									s.getInputStream()));
					// 持续接收客户端信息
					String message = br.readLine();
//					String message = receiveMessage(s);
					if (message == null) {// 有人退出
						allSockets.remove(s);
						sendMessageToClient("有人退出聊天室！");
						break;
					}
					// 返回信息给所有客户端
					sendMessageToClient(message);
				}
			} catch (Exception e) {
//				allSockets.clear();
				e.printStackTrace();
			}
		}
		/**
		 * 自定义方法，用来接收信息
		 * 一个一个连接来获取信息
		 * 针对一个连接操作，读一个连接信息
		 * 连接已知
		 */
		private String receiveMessage(Socket s) throws Exception {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							s.getInputStream()));
			String message = br.readLine();
			if(message == null) {// 有人退出
				allSockets.remove(s);
				return "有人退出聊天室！";
			}
			return message;// 将接收到的信息作为返回值
		}
	}
	
	
	
}
