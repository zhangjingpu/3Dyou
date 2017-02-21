 using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class Goto2d : MonoBehaviour {
	public Button gotoTwoD;
	private SocketHelper sockethelper;

	void Start () {
		sockethelper = SocketHelper.GetInstance ();
		gotoTwoD.onClick.AddListener (gotoTwoDButton);
	}

	void gotoTwoDButton(){
		
	//	sockethelper.SendMessage ("/exit");
		//SocketHelper.getSocket ().Close ();
		Application.LoadLevel ("new1");
	//	sockethelper.SendMessage("猪");
	}

}
