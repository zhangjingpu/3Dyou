package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import knowcreater.Tool;
import net.paoding.analysis.FenciTest;

public class Client implements Runnable {
	private Socket clientSocket = null;
	private PrintWriter pw;
	private FenciTest fenci = new FenciTest();
	private byte[] message;
	BufferedInputStream bis;
	BufferedReader br;
	public PrintWriter getPrintWriter(){
		return pw;
	}
	
	public Client(Socket client) {
		try {
			Tool.getPrintWriter().println("Client : 连接 . ");
			this.clientSocket = client;
			pw = new PrintWriter(clientSocket.getOutputStream(), true);
			bis = new BufferedInputStream(client.getInputStream());
			Tool.getPrintWriter().println("Client . Inputstream in done . ");
			message = new byte[1024];
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("finally")
	public void run() {
		if(MyServer.flag) {
			Tool.getPrintWriter().println("Client : Goto 3d method .");
			return ;
		}
		int count = 0;
		String content = "", questionContent = "", answerContent = "";
		Tool.getPrintWriter().println("Client : The th in ArrayList is " + (MyServer.getSocketList().indexOf(clientSocket) + 1)
				+ " \nClient : The user has been online . ");
		int result, exitResult = "/exit".hashCode(), studyResult = "/study".hashCode(), doneResult = "。".hashCode(),
				quitResult = "/quit".hashCode();
		try {
			while (true) {
				Tool.getPrintWriter().println("Client : Run in while()");
				bis.read(message);
				content = new String(message, "utf-8");
				Tool.getPrintWriter().println("Content : " + content);
				message = new byte[message.length];
				Tool.getPrintWriter().println("Content lenghth : " + content.length());
				if (content.indexOf("/goto3D") != -1 || content.hashCode() == "/goto3D".hashCode())
				{
					Tool.getPrintWriter().println("Client : In here ." );
					MyServer.flag = true;
					MyServer.check = true;
					Tool.getPrintWriter().println("Client : Flag changed to true .");
					throw new IOException();
				}
				result = content.toLowerCase().trim().hashCode();
				if (result == doneResult) {
					Tool.getPrintWriter().println(("Ke'da'xun'fei's over . "));
					continue;
				} else if (result == exitResult || content.trim().length() == 0) {
					if ((content.trim().length() == 0 ? ++count : count) >= 3) {
						throw new IOException();
					} else if (result != exitResult)
						continue;
					Tool.getPrintWriter().println(content);
					Tool.getPrintWriter().println("/exit");
					throw new IOException();
				} else if (result == studyResult) {
					Tool.getPrintWriter().println("The user open the learn mode . ");
					byte[] question = new byte[1024];
					byte[] answer = new byte[1024];
					while (true) {
						bis.read(question);
						questionContent = new String(question, "utf-8").trim().toLowerCase();
						Tool.getPrintWriter().println("QuestionContent : " + questionContent);
						result = questionContent.hashCode();
						if (questionContent.length() == 0) {
							Tool.getPrintWriter().println("Error in client . /sutdy with \" \"");
							break;
						} else if (result == quitResult) {
							Tool.getPrintWriter().println("The user close the learn mode . ");
							break;
						} else {
							bis.read(answer);

							// }
							answerContent = new String(answer, "utf-8");
							Tool.getPrintWriter().println("AnswerContent : " + answerContent);
							fenci.Learn(questionContent, answerContent);
							pw.println(fenci.getOutput());
						}
						question = new byte[1024];
						answer = new byte[1024];
					}
				} else {
					fenci.run(content);
					Tool.getPrintWriter().println("Fenci Output : " + fenci.getOutput());
					if(!MyServer.flag){
						pw.println(fenci.getOutput());
						Tool.getPrintWriter().println("Client : Send message to client .");
					}else{
						pw.println(fenci.getOutput());
						for(int i = 0 ; i < MyServer.getU3DSocket().size() ; i++){
							new PrintWriter(MyServer.getU3DSocket().get(i).getOutputStream(),true).println(fenci.getOutput());
						}
						Tool.getPrintWriter().println("Send message to 3d . ");
					}
				}
			}
		} catch (IOException e) {
			Tool.getPrintWriter().println("Catch IOException : " + "The user has been offline");
			// MyServer.flag = false;
			// Tool.getPrintWriter().println("Client : IOException and change the flag . ");
			if(MyServer.check) {
				MyServer.flag = true;
				MyServer.getU3DSocket().clear();
				MyServer.check = false;
				Tool.getPrintWriter().println("Client : clear the U3D SocketList . run in if .");
			}else{
				MyServer.flag = false;
				MyServer.U3DSocketList.clear();
				Tool.getPrintWriter().println("Client : claer the U3D SocketList . run in else .");
			}
		} finally {
			if (clientSocket != null) {
				MyServer.getSocketList().remove(clientSocket);
				clientSocket = null;
			}
			return;
		}
	}
}
