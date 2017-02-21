using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
public class Send : MonoBehaviour {
	public UILabel a_lable;
	public GameObject a_table;
	public GameObject send;
	public GameObject study;
	public GameObject chat;
	private GameObject obj1;
	private GameObject obj2;
	private	int a = 0;
	private  string text = "";
	private string a_info = "";
	private string content="";
	private SocketHelper sockethelper;
	private string answer = "";
	private bool ifstudy = false;
	float boomTimer = 2f; 
	bool goOn = false;
	static int returnDeal =0;
    void Update() {
        if (Application.platform == RuntimePlatform.Android && (Input.GetKeyDown(KeyCode.Escape))) {
            
        }
    }
	void Start(){
		sockethelper = SocketHelper.GetInstance ();
		UIEventListener.Get(send).onClick += OnButtonclicksend;
		UIEventListener.Get (study).onClick += OnButtonclickstudy;
		UIEventListener.Get (chat).onClick += OnButtonclickchat;

	}
	void OnButtonclickstudy(GameObject obj){
		sockethelper.SendMessage ("/study");
		ifstudy = true;
	}
	void OnButtonclickchat(GameObject obj){

		sockethelper.SendMessage("/quit");
	}
	void OnButtonclicksend(GameObject button)
	{ 	
		content = a_lable.text;
		sockethelper.SendMessage (content);
		dealAction(content);
       AnimationCro.returnAnim(returnDeal);
		text = a_lable.text;
		makeAnswerChangeUI ();
		content = "";
		if (!ifstudy) {
			answer = sockethelper.getAnswer ();
			while (answer==null||answer=="") {
				answer = sockethelper.getAnswer();
			}
			answer = answer.Substring(0,answer.Length-1);
		}

		if ( answer!=null&&!answer.Equals("")) {
			Debug.Log("answer");
			getAnswerChangeUI();
		}
	}
	
	private  void dealAction(string str) {
		if(str.IndexOf("篮球") != -1){
				Debug.Log("听见有篮球");
				returnDeal = 1;
			}
		else if(str.IndexOf("足球") != -1){
				Debug.Log("听见有足球");
				returnDeal = 2;
			}
        else if (str.IndexOf("转球") != -1) {
            Debug.Log("听见有在转球");
            returnDeal = 3;
        }
        else if (str.IndexOf("抬腿") != -1) {
            returnDeal = 4;
        }
        else if (str.IndexOf("拍手") !=-1) 
        {
            returnDeal = 5;
        }
        else if (str.IndexOf("不") != -1) {
            returnDeal = 6;
        }
		else if (str.IndexOf("体操") != -1) {
			returnDeal = 7;
		}
		else if (str.IndexOf("正步") != -1) {
			returnDeal = 8;
		}
		else if (str.IndexOf("踏步") != -1) {
			returnDeal = 9;
		}

	}

	void makeAnswerChangeUI(){
		Vector3 vector = new Vector3 (a_table.transform.position.x,
		                              a_table.transform.position.y,
		                              a_table.transform.position.z);
		obj1 = (GameObject)GameObject.Instantiate (Resources.Load("Quest9"),
		                                           vector,Quaternion.identity);
		obj1.transform.parent = a_table.transform;
		obj1.transform.localScale = new Vector3(1,1,1);
		foreach (Transform child in obj1.transform)
		{
			if( child.gameObject.name .Equals("title") ){			
				UILabel label2 =obj1.GetComponentInChildren<UILabel>();
				label2.text = "[[aa6333]46[-]]me"+a;
			}
			else if(child.gameObject.name.Equals("Tween")){
				GameObject tween = child.gameObject;		
				UILabel label3 = tween.GetComponentInChildren<UILabel> ();				
				label3.text = text;
				a_lable.text = "";
			}
		}
	}
	void getAnswerChangeUI(){
		Vector3 vector2 = new Vector3 (a_table.transform.position.x,
		                               a_table.transform.position.y,a_table.transform.position.z);

		obj2 = (GameObject)GameObject.Instantiate (Resources.Load("Quest9"),
		                                           vector2,Quaternion.identity);

		obj2.transform.parent = a_table.transform;
		obj2.transform.localScale = new Vector3(1,1,1);
		foreach (Transform child in obj2.transform)
		{
			if( child.gameObject.name .Equals("title") ){			
				UILabel label4 =obj2.GetComponentInChildren<UILabel>();
				label4.text = "[[FF6333]58[-]]answer"+a;

				
			}
			else if(child.gameObject.name.Equals("Tween")){
				GameObject tween = child.gameObject;		
				UILabel label5 = tween.GetComponentInChildren<UILabel> ();				
				label5.text = answer;

				
				sockethelper.setAnswer();
				answer = "";
				answer = null;

			}
		}
	}

}
