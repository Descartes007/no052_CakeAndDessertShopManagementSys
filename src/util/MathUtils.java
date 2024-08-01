package util;

import java.text.DecimalFormat;


public class MathUtils {
	
	public static Double getTwoDouble(Double d) {
		String str = new DecimalFormat("#.00").format(d);
		return Double.parseDouble(str);
		
	}
	
	
}
