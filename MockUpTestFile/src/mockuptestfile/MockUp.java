package mockuptestfile;

public class MockUp {
	
	public static String returning(double apiWidth,double apiHeight,double sD,double cD){
		String s = apiWidth + " , " + apiHeight + " , " + sD + " , " + cD;
		return s;
	}

	public static void main(String[] args) {
		//System.out.println(returning(14.299,4673.484,177.34957,6578.2435798));
		System.out.println(returning(Double.parseDouble(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3])));
	}

}
