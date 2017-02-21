using UnityEngine;
using System.Collections;
using UnityEngine.UI;
public class QuitIn3D : MonoBehaviour {
	public Button quitIn3d;
	private SocketHelper sockethelper;
	
	void Start () {
		sockethelper = SocketHelper.GetInstance ();
		quitIn3d.onClick.AddListener (quitIn3DButton);
	}
	
	void quitIn3DButton(){
		
		sockethelper.SendMessage ("/exit");
		SocketHelper.getSocket ().Close ();
		Application.Quit ();
		//	sockethelper.SendMessage("猪");
	}

}
