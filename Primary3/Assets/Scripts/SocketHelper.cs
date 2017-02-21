using System;  
using System.Collections.Generic;  
using System.Linq;  
using System.Text;  
using System.Net;  
using System.Net.Sockets;  
using System.Threading;  
using UnityEngine;  

public class SocketHelper  
{  
	
	private static SocketHelper socketHelper=new SocketHelper();  
	
	private static Socket socket;  

	private string answer;

	public static SocketHelper GetInstance()  
	{  
		return socketHelper;  
	}  

	public static Socket getSocket(){
        //if (SocketHelper.socket == null) {
       //   socketHelper = new SocketHelper();
      // }
		return SocketHelper.socket;
	}
    public static void setSocket(){
        SocketHelper.socket = null;
    }
    
    public SocketHelper() {  
		
        //采用TCP方式连接  
        socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);  
		
        //服务器IP地址  
        IPAddress address = IPAddress.Parse("211.87.226.188");  
		
        //服务器端口  
        IPEndPoint endpoint = new IPEndPoint(address,8888);  
		
        //异步连接,连接成功调用connectCallback方法  
        IAsyncResult result = socket.BeginConnect(endpoint, new AsyncCallback(ConnectCallback), socket);  
		
        //这里做一个超时的监测，当连接超过5秒还没成功表示超时  
        bool success = result.AsyncWaitHandle.WaitOne(5000, true);  
        if (!success)  
        {  
            //超时  
            Closed();  
            Debug.Log("connect Time Out");  
        }  
        else  
        {  
            //与socket建立连接成功，开启线程接受服务端数据。  
            Thread thread = new Thread(new ThreadStart(ReceiveSorket));  
            thread.IsBackground = true;  
            thread.Start();  
        }  
		
    }  
	
    private void ConnectCallback(IAsyncResult asyncConnect)  
    {  
        Debug.Log("connect success");  
    }  
	
    private void ReceiveSorket()  
    {  
        while (true)  
        {  
			
            if (!socket.Connected)  
            {  
                Debug.Log("Failed to clientSocket server.");  
                socket.Close();  
                break;  
            }  
            try  
            {  
                string str = "";
                byte[] bytes = new byte[4096];  

                int i = socket.Receive(bytes);  
                if (i <= 0)  
                {  
                    socket.Close();  
                    break;  
                }  
                str+=Encoding.UTF8.GetString(bytes,0,i);
                answer = str;
                Debug.Log(str);
            }  
            catch (Exception e)  
            {  
                Debug.Log("Failed to clientSocket error." + e);  
                socket.Close();  
                break;  
            }  
        }  
    }  
	
    public string getAnswer(){
        return answer;
    }
    public void setAnswer(){
        answer = "";
    }
    //关闭Socket  
    public void Closed()  
    {  
        if (socket != null && socket.Connected)  
        {  
            socket.Shutdown(SocketShutdown.Both);  
            socket.Close();  
        }  
        socket = null;  
    }  

    public void SendMessage(string str)  
    {  
        byte[] msg = Encoding.UTF8.GetBytes(str);  
		
        if (!socket.Connected||msg.Length==0)  
        {  
            socket.Close();  
            return;  
        }  
        try  
        {  
            IAsyncResult asyncSend = socket.BeginSend(msg, 0, msg.Length, SocketFlags.None, new AsyncCallback(SendCallback), socket);  
            bool success = asyncSend.AsyncWaitHandle.WaitOne(5000, true);  
            if (!success)  
            {  
                socket.Close();  
                Debug.Log("Failed to SendMessage server.");  
            }  
        }  
        catch  
        {  
            Debug.Log("send message error");  
        }  
    }  
	
    private void SendCallback(IAsyncResult asyncConnect)  
    {  
        Debug.Log("send success");  
    }  
	
   
}  