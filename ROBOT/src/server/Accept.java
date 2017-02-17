package server;

import java.net.Socket;
import knowcreater.Tool;

class Accept extends Thread {
	public Socket client;

	public Accept(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		// client = MyServer.getServerSocket().accept();
		// Tool.getPrintWriter().println("Info\t" +
		// client.getLocalSocketAddress());
		// Tool.getPrintWriter().println(client.getInetAddress());
		// getSocketList().add(client);
		if (MyServer.flag) {
			Tool.getPrintWriter()
					.println("Accept : 3D is online . \nServer : " + MyServer.getU3DSocket().size() + " Connect . ");
			while (true) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// return;
		} else {
			Tool.getPrintWriter().println("Accept : Server : " + MyServer.getSocketList().size() + " Connect . ");
			Tool.getPrintWriter().println("Accept : 用户端非3d连接.");
			Tool.getPrintWriter().println("Accept : 测试是否空 : " + (client == null));
			new Client(client);
		}
	}
}