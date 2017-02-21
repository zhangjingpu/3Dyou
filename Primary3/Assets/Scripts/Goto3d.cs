using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class Goto3d : MonoBehaviour {
	// Use this for initialization
	public GameObject goto3dButton;
	private SocketHelper sockethelper;
	void Start () {
		sockethelper = SocketHelper.GetInstance ();
		UIEventListener.Get (goto3dButton).onClick += goto3dOnclick;
	}
	void goto3dOnclick(GameObject obj){
		sockethelper.SendMessage ("/goto3D");
		SocketHelper.getSocket ().Close ();
        //  SocketHelper.setSocket();
		Application.LoadLevel ("new4");
		//sockethelper.SendMessage("傻逼");
	}

}
