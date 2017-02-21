using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;

public class AndroidAPP : MonoBehaviour {
	private string words="显示Android对话框";
	// Android Activity
	private AndroidJavaObject activity;
	private string answer;
	// Use this for initialization
    void Update()
    {
        if (Application.platform == RuntimePlatform.Android && (Input.GetKeyDown(KeyCode.Escape))) {
            Application.LoadLevel("new1");
        }
    }
	void Start () {
       
	this.name = "AndroidManager";
	// 获得Android Activity
		AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		activity = jc.GetStatic<AndroidJavaObject>("currentActivity");
	}
	void OnGUI()
	{
        
		if (GUI.Button(new Rect(0, 0, 500, 300), "点击开始语音监听"))
		{
            Debug.Log("GUIButton");
			string[] args=new string[2];
			args[0]="";
			args[1]="World";
			activity.Call("HelloWorld", args);
		}

	}
	 void AndroidCallBack(string str)
	{
		if (str.Equals ("1")) {
			AnimationCro.returnAnim (1);
		} else if (str.Equals ("2")) {
			AnimationCro.returnAnim (2);
		} else if (str.Equals ("3")) {
			AnimationCro.returnAnim (3);
		} else if (str.Equals ("4")) {
			AnimationCro.returnAnim (4);
		} else if (str.Equals ("5")) {
			AnimationCro.returnAnim (5);
		} else if (str.Equals ("6")) {
			AnimationCro.returnAnim (6);
		} else if (str.Equals ("7")) {
			AnimationCro.returnAnim(7);
		} else if (str.Equals ("8")) {
			AnimationCro.returnAnim(8);
		} else if (str.Equals ("9")) {
			AnimationCro.returnAnim(9);
		}
	
	}
}
