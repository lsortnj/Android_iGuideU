package mo.iguideu.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

public class MoAnimationUtil {

	public static Animation getFadeInAnimation(int duration){
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
		fadeIn.setDuration(duration);
		
		return fadeIn;
	}
}
