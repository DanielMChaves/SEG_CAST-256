import java.math.BigInteger;
import java.security.Provider;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class cast_256 {

	// Configuration
	private static final String KEY_ALGO = "CAST6";
	private static final String CIPHER_ALGO = "CAST6/ECB/NOPADDING";
	private static Provider bc = new BouncyCastleProvider();

	// Text Vectors
	private static String keytext = "2342bb9efa38542c0af75647f29f615d"; 	// 32 car * 4 bits/car  = 128 bits (Hasta 256 bits)
	private static String plaintext = "00000000000000000000000000000000"; 	// 32 car * 4 bits/car  = 128 bits
	private static String ciphertext = "c842a08972b43d20836c91d1b7530f6b"; 	// 32 car * 4 bits/car  = 128 bits

	// Test Size Range
	private static final int inicial = 0;
	private static final int end = 128;
	// (Vectors to Study the Avalanche Effect at End of File)

	private static double[] frecuency = new double[32];

	// Encrypt Funtion
	static String encrypt(String plaintext) throws Exception {

		Cipher cipher = Cipher.getInstance(CIPHER_ALGO, bc);

		byte[] keyBytes = Hex.decodeHex(keytext.toCharArray());

		SecretKeySpec key = new SecretKeySpec(keyBytes, KEY_ALGO);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] input = Hex.decodeHex(plaintext.toCharArray());
		byte[] output = cipher.doFinal(input);
		String actual = Hex.encodeHexString(output);
		return actual;
	}

	// Hexadecimal to Binary Funtion
	static String hexToBin(String s) { 
		return new BigInteger(s, 16).toString(2);
	}

	// Check Binary Funtion
	// Fill with zeros to the left until you reach 128 Characters
	private static String checkBinary(String number) {

		char[] binary = number.toCharArray();
		int different = 128 - binary.length;
		String newNumber = "";

		if(different == 0){
			return number;
		}

		int j = 0;

		for(int i = 0; i < different ; i++){
			newNumber = newNumber + "0";
		}

		for(int i = different; i < 128 ; i++){
			newNumber = newNumber + binary[j];
			j++;
		}

		return newNumber;
	}

	// Get Hamming Distance Funtion
	private static int getHammingDistance(String sequence1, String sequence2) {

		char[] s1 = sequence1.toCharArray();
		char[] s2 = sequence2.toCharArray();

		int result = 0;

		for (int i = 0; i< s1.length; i++) {
			if (s1[i] != s2[i]) result++;
		}

		return result;
	}

	// Pre Frecuency Hamming Distance Funcion
	private static void preFrecuencyHammingDistance(double[] frecuency2){
		for(int i = 0; i < frecuency2.length; i++){
			frecuency2[i] = 0;
		}
	}

	// Frecuency Hamming Distance Funcion
	private static void frecuencyHammingDistance(int hamming, double[] frecuency2){

		switch(hamming){
		case 50: frecuency2[0] += 1; break;
		case 51: frecuency2[1] += 1; break;
		case 52: frecuency2[2] += 1; break;
		case 53: frecuency2[3] += 1; break;
		case 54: frecuency2[4] += 1; break;
		case 55: frecuency2[5] += 1; break;
		case 56: frecuency2[6] += 1; break;
		case 57: frecuency2[7] += 1; break;
		case 58: frecuency2[8] += 1; break;
		case 59: frecuency2[9] += 1; break;
		case 60: frecuency2[10] += 1; break;
		case 61: frecuency2[11] += 1; break;
		case 62: frecuency2[12] += 1; break;
		case 63: frecuency2[13] += 1; break;
		case 64: frecuency2[14] += 1; break;
		case 65: frecuency2[15] += 1; break;
		case 66: frecuency2[16] += 1; break;
		case 67: frecuency2[17] += 1; break;
		case 68: frecuency2[18] += 1; break;
		case 69: frecuency2[19] += 1; break;
		case 70: frecuency2[20] += 1; break;
		case 71: frecuency2[21] += 1; break;
		case 72: frecuency2[22] += 1; break;
		case 73: frecuency2[23] += 1; break;
		case 74: frecuency2[24] += 1; break;
		case 75: frecuency2[25] += 1; break;
		case 76: frecuency2[26] += 1; break;
		case 77: frecuency2[27] += 1; break;
		case 78: frecuency2[28] += 1; break;
		case 79: frecuency2[29] += 1; break;
		case 80: frecuency2[30] += 1; break;
		case 81: frecuency2[31] += 1; break;
		}

	}

	public static void main(String[] args) throws Exception {

		int turn = 50;
		preFrecuencyHammingDistance(frecuency);
		double counterMedia = 0.0;
		double counterVariance = 0.0;
		final double media = 63.984375;
		
		for(int i = inicial; i <= end; i++){

			String msg = vectors[i];
			String hexToBinActual;
			int distance;

			System.out.println("================================================================");
			System.out.println("Encrypting Turn: " + i);
			System.out.println("================================================================");

			String actual = encrypt(msg);

			System.out.println("actual (Hex): " + actual);
			System.out.println("expect (Hex): " + ciphertext);

			hexToBinActual = hexToBin(actual);

			System.out.println("actual (Bin): " + checkBinary(hexToBinActual));
			System.out.println("expect (Bin): " + hexToBin(ciphertext));
			
			distance = getHammingDistance(checkBinary(hexToBinActual), hexToBin(ciphertext));
			
			System.out.println("Hamming Distance: " + distance);

			frecuencyHammingDistance(distance, frecuency);
			
			// Estadistic Data
			counterMedia = counterMedia + distance;
			counterVariance = counterVariance + Math.pow(distance - media, 2);
			
		}
		
		System.out.println("================================================================");
		System.out.println("Frecuency Hamming Distance");
		System.out.println("================================================================");
		
		for(int i = 0; i < frecuency.length; i++){
			System.out.println("Frecuency of " + turn + " is: " + frecuency[i]);
			turn++;
		}
		
		System.out.println("================================================================");
		System.out.println("Percentage Hamming Distance");
		System.out.println("================================================================");
		
		double percentage;
		turn = 50;
		
		for(int i = 0; i < frecuency.length; i++){
			percentage = 0.0;
			percentage = (frecuency[i] * 100) / 128;
			System.out.println("Percentage of " + turn + " is: " + percentage);
			turn++;
		}
		
		System.out.println("================================================================");
		System.out.println("Estadistic Data");
		System.out.println("================================================================");
		System.out.println("Media is: " + counterMedia/128);
		System.out.println("Variance is: " + counterVariance/128);
		System.out.println("Deviation is: " + Math.sqrt(counterVariance/128));
		
	}

	// Vectors to Study the Avalanche Effect (Hexadecimal)
	private static String v001 = "80000000000000000000000000000000";
	private static String v002 = "40000000000000000000000000000000";
	private static String v003 = "20000000000000000000000000000000";
	private static String v004 = "10000000000000000000000000000000";
	private static String v005 = "08000000000000000000000000000000";
	private static String v006 = "04000000000000000000000000000000";
	private static String v007 = "02000000000000000000000000000000";
	private static String v008 = "01000000000000000000000000000000";
	private static String v009 = "00800000000000000000000000000000";
	private static String v010 = "00400000000000000000000000000000";
	private static String v011 = "00200000000000000000000000000000";	
	private static String v012 = "00100000000000000000000000000000";	
	private static String v013 = "00080000000000000000000000000000";	
	private static String v014 = "00040000000000000000000000000000";
	private static String v015 = "00020000000000000000000000000000";
	private static String v016 = "00010000000000000000000000000000";

	private static String v017 = "00008000000000000000000000000000";	
	private static String v018 = "00004000000000000000000000000000";	
	private static String v019 = "00002000000000000000000000000000";	
	private static String v020 = "00001000000000000000000000000000";	
	private static String v021 = "00000800000000000000000000000000";	
	private static String v022 = "00000400000000000000000000000000";	
	private static String v023 = "00000200000000000000000000000000";	
	private static String v024 = "00000100000000000000000000000000";	
	private static String v025 = "00000080000000000000000000000000";
	private static String v026 = "00000040000000000000000000000000";	
	private static String v027 = "00000020000000000000000000000000";	
	private static String v028 = "00000010000000000000000000000000";	
	private static String v029 = "00000008000000000000000000000000";	
	private static String v030 = "00000004000000000000000000000000";	
	private static String v031 = "00000002000000000000000000000000";	
	private static String v032 = "00000001000000000000000000000000";

	private static String v033 = "00000000800000000000000000000000";
	private static String v034 = "00000000400000000000000000000000";
	private static String v035 = "00000000200000000000000000000000";
	private static String v036 = "00000000100000000000000000000000";
	private static String v037 = "00000000080000000000000000000000";
	private static String v038 = "00000000040000000000000000000000";
	private static String v039 = "00000000020000000000000000000000";
	private static String v040 = "00000000010000000000000000000000";
	private static String v041 = "00000000008000000000000000000000";
	private static String v042 = "00000000004000000000000000000000";
	private static String v043 = "00000000002000000000000000000000";
	private static String v044 = "00000000001000000000000000000000";
	private static String v045 = "00000000000800000000000000000000";
	private static String v046 = "00000000000400000000000000000000";
	private static String v047 = "00000000000200000000000000000000";
	private static String v048 = "00000000000100000000000000000000";

	private static String v049 = "00000000000080000000000000000000";
	private static String v050 = "00000000000040000000000000000000";
	private static String v051 = "00000000000020000000000000000000";
	private static String v052 = "00000000000010000000000000000000";
	private static String v053 = "00000000000008000000000000000000";
	private static String v054 = "00000000000004000000000000000000";
	private static String v055 = "00000000000002000000000000000000";
	private static String v056 = "00000000000001000000000000000000";
	private static String v057 = "00000000000000800000000000000000";
	private static String v058 = "00000000000000400000000000000000";
	private static String v059 = "00000000000000200000000000000000";
	private static String v060 = "00000000000000100000000000000000";
	private static String v061 = "00000000000000080000000000000000";
	private static String v062 = "00000000000000040000000000000000";
	private static String v063 = "00000000000000020000000000000000";
	private static String v064 = "00000000000000010000000000000000";

	private static String v065 = "00000000000000008000000000000000";
	private static String v066 = "00000000000000004000000000000000";
	private static String v067 = "00000000000000002000000000000000";
	private static String v068 = "00000000000000001000000000000000";
	private static String v069 = "00000000000000000800000000000000";
	private static String v070 = "00000000000000000400000000000000";
	private static String v071 = "00000000000000000200000000000000";
	private static String v072 = "00000000000000000100000000000000";
	private static String v073 = "00000000000000000080000000000000";
	private static String v074 = "00000000000000000040000000000000";
	private static String v075 = "00000000000000000020000000000000";
	private static String v076 = "00000000000000000010000000000000";
	private static String v077 = "00000000000000000008000000000000";
	private static String v078 = "00000000000000000004000000000000";
	private static String v079 = "00000000000000000002000000000000";
	private static String v080 = "00000000000000000001000000000000";

	private static String v081 = "00000000000000000000800000000000";
	private static String v082 = "00000000000000000000400000000000";
	private static String v083 = "00000000000000000000200000000000";
	private static String v084 = "00000000000000000000100000000000";
	private static String v085 = "00000000000000000000080000000000";
	private static String v086 = "00000000000000000000040000000000";
	private static String v087 = "00000000000000000000020000000000";
	private static String v088 = "00000000000000000000010000000000";
	private static String v089 = "00000000000000000000008000000000";
	private static String v090 = "00000000000000000000004000000000";
	private static String v091 = "00000000000000000000002000000000";
	private static String v092 = "00000000000000000000001000000000";
	private static String v093 = "00000000000000000000000800000000";
	private static String v094 = "00000000000000000000000400000000";
	private static String v095 = "00000000000000000000000200000000";
	private static String v096 = "00000000000000000000000100000000";

	private static String v097 = "00000000000000000000000080000000";
	private static String v098 = "00000000000000000000000040000000";
	private static String v099 = "00000000000000000000000020000000";
	private static String v100 = "00000000000000000000000010000000";
	private static String v101 = "00000000000000000000000008000000";
	private static String v102 = "00000000000000000000000004000000";
	private static String v103 = "00000000000000000000000002000000";
	private static String v104 = "00000000000000000000000001000000";
	private static String v105 = "00000000000000000000000000800000";
	private static String v106 = "00000000000000000000000000400000";
	private static String v107 = "00000000000000000000000000200000";
	private static String v108 = "00000000000000000000000000100000";
	private static String v109 = "00000000000000000000000000080000";
	private static String v110 = "00000000000000000000000000040000";
	private static String v111 = "00000000000000000000000000020000";
	private static String v112 = "00000000000000000000000000010000";

	private static String v113 = "00000000000000000000000000008000";
	private static String v114 = "00000000000000000000000000004000";
	private static String v115 = "00000000000000000000000000002000";
	private static String v116 = "00000000000000000000000000001000";
	private static String v117 = "00000000000000000000000000000800";
	private static String v118 = "00000000000000000000000000000400";
	private static String v119 = "00000000000000000000000000000200";
	private static String v120 = "00000000000000000000000000000100";
	private static String v121 = "00000000000000000000000000000080";
	private static String v122 = "00000000000000000000000000000040";
	private static String v123 = "00000000000000000000000000000020";
	private static String v124 = "00000000000000000000000000000010";
	private static String v125 = "00000000000000000000000000000008";
	private static String v126 = "00000000000000000000000000000004";
	private static String v127 = "00000000000000000000000000000002";
	private static String v128 = "00000000000000000000000000000001";

	private static String[] vectors = {plaintext,
			v001,v002,v003,v004,v005,v006,v007,v008,v009,v010,v011,v012,v013,v014,v015,v016,
			v017,v018,v019,v020,v021,v022,v023,v024,v025,v026,v027,v028,v029,v030,v031,v032,
			v033,v034,v035,v036,v037,v038,v039,v040,v041,v042,v043,v044,v045,v046,v047,v048,
			v049,v050,v051,v052,v053,v054,v055,v056,v057,v058,v059,v060,v061,v062,v063,v064,
			v065,v066,v067,v068,v069,v070,v071,v072,v073,v074,v075,v076,v077,v078,v079,v080,
			v081,v082,v083,v084,v085,v086,v087,v088,v089,v090,v091,v092,v093,v094,v095,v096,
			v097,v098,v099,v100,v101,v102,v103,v104,v105,v106,v107,v108,v109,v110,v111,v112,
			v113,v114,v115,v116,v117,v118,v119,v120,v121,v122,v123,v124,v125,v126,v127,v128};

}