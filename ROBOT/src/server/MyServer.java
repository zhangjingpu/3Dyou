package server;
import java.util.ArrayList;
import knowcreater.Tool;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class MyServer {
	private static ArrayList<Socket> socketList = new ArrayList<Socket>();
	public static boolean flag = false;
	public static boolean check = false;
	public static ArrayList<Socket> getSocketList() {
		return socketList;
	}

	private static ServerSocket serverSocket;

	public Socket client;

	public static ArrayList<Socket> U3DSocketList = new ArrayList<Socket>();

	public static ArrayList<Socket> getU3DSocket() {
		return U3DSocketList;
	}

	public MyServer(Socket client) {
		this.client = client;
	}

	// @SuppressWarnings("resource")
	// private static void init() {
	// try {
	// serverSocket = new ServerSocket(8888);
	// while (true) {
	// Tool.getPrintWriter().println("Start thread here . ");
	// Socket client = serverSocket.accept();
	// Tool.getPrintWriter().println("Info\t" + client.getLocalSocketAddress());
	// Tool.getPrintWriter().println(client.getInetAddress());
	// getSocketList().add(client);
	// Tool.getPrintWriter().println("Server : " + getSocketList().size() + "
	// Connect . ");
	// // new MyThread().run(client);
	// // new Client(client);
	// }
	// } catch (IOException e) {
	// Logger.getLogger("Server").info("Server Close .");
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) throws IOException {
		// init();
		ServerSocket serverSocket = new ServerSocket(8888);
		while (true) {
			Tool.getPrintWriter().println("Myserver : Start thread here . ");
			Socket client = serverSocket.accept();
			if (flag) {
				Tool.getPrintWriter().println("Myserver : Get 3d Socket .");
				getU3DSocket().add(client);
				Tool.getPrintWriter().println("Myserver : U3D socket.length() : " + getU3DSocket().size());
				Tool.getPrintWriter().println("Myserver : U3D socketIP : " + client.getInetAddress());
			} else {

				Tool.getPrintWriter().println("Myserver : Socket IP : " + client.getInetAddress());
				getSocketList().add(client);
			}
			Tool.getPrintWriter().println("Myserver : new Accept().run()");
			Accept accept = new Accept(client);
			accept.start();
			Tool.getPrintWriter().println("Myserver : Info : " + client.getLocalSocketAddress());
			Tool.getPrintWriter().println("Myserver : " + client.getInetAddress());
			Tool.getPrintWriter().println("MyServer Class debug : " + getSocketList().size() + " Connect . ");
		} 
	}

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	// class Accept implements Runnable {
	// public Socket client;
	//
	// public Accept(Socket client) {
	// this.client = client;
	// }
	//
	// @Override
	// public void run() {
	// try {
	// // client = MyServer.getServerSocket().accept();
	// // Tool.getPrintWriter().println("Info\t" +
	// // client.getLocalSocketAddress());
	// // Tool.getPrintWriter().println(client.getInetAddress());
	// // getSocketList().add(client);
	// new Client(client);
	// Tool.getPrintWriter().println("Server : " + getSocketList().size() + "
	// Connect . ");
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	//
	// }
	// }
}
