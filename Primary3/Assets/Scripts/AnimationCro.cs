using UnityEngine;
using System.Collections;

public class AnimationCro : MonoBehaviour {
	
	static Animator a_ani;
	public  GameObject person1;
    static string oldstate ;
    static bool First = true;
    static bool Second = true;
	// Use this for initialization
	void Start () {
        a_ani = this.GetComponent<Animator>();
	}
    public static void returnAnim(int i) {
        if (i == 1) {
			animChange ("basket");
		} else if (i == 2) {
			animChange ("football");
		} else if (i == 3) {
			animChange ("turn");
		} else if (i == 4) {
			animChange ("pop");
		} else if (i == 5) {
			animChange ("saygood");
		} else if (i == 6) {
			animChange ("no");
		} else if (i == 7) {
			animChange ("exesize");
		} else if (i == 8) {
			animChange ("zhengbu");
		} else if (i == 9) {
			animChange("tabu");
		}
    }
	public static void animChange (string nextstate) {
		AnimatorStateInfo stateInfo = a_ani.GetCurrentAnimatorStateInfo (0);

        if (oldstate!=null) {
            a_ani.SetBool(oldstate, false);
        }
        	oldstate = nextstate;
		    a_ani.SetBool (nextstate, true);
		    Debug.Log("调用动画后");
    }

}
