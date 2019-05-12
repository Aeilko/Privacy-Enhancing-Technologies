package utility;

public class ArrayUtil {

	public static byte[] append(byte[] a, byte[] b){
		byte[] result = new byte[a.length+b.length];

		for(int i = 0; i < a.length; i++){
			result[i] = a[i];
		}
		for(int i = 0; i < b.length; i++){
			result[a.length+i] = b[i];
		}

		return result;
	}

	public static void display(Object[] a){
		for(int i = 0; i < a.length; i++){
			System.out.println(a[i].toString());
		}
	}

	public static void display(byte[] a){
		for(int i = 0; i < a.length; i++){
			String s1 = String.format("%8s", Integer.toBinaryString(a[i] & 0xFF)).replace(' ', '0');
			System.out.println(s1);
		}
	}
}
