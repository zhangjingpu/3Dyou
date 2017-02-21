using UnityEngine;
using System.Collections;

public class QuitIn2D : MonoBehaviour {
	public GameObject quitIn2d;
	private SocketHelper sockethelper;
	void Start () {
		sockethelper = SocketHelper.GetInstance ();
		UIEventListener.Get (quitIn2d).onClick += quitIn2D;
	}
	void quitIn2D(GameObject obj){
		sockethelper.SendMessage ("/exit");
		SocketHelper.getSocket ().Close ();
        //  SocketHelper.setSocket();
		Application.Quit ();
		//sockethelper.SendMessage("傻逼");
	}
}
