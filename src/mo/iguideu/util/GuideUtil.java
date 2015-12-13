package mo.iguideu.util;

import mo.iguideu.guide.GuideEvent;

public class GuideUtil {

	private static GuideEvent targetGuide = null;

	public static GuideEvent getTargetGuide() {
		return targetGuide;
	} 

	public static void setTargetGuide(GuideEvent targetGuide) {
		GuideUtil.targetGuide = targetGuide;
	}
	
	
	
}
