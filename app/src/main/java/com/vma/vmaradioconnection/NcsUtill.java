package com.vma.vmaradioconnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NcsUtill {

	
	public static String encodeHexString(byte[] byteArray) {
	    StringBuffer hexStringBuffer = new StringBuffer();
	    for (int i = 0; i < byteArray.length; i++) {
	        hexStringBuffer.append(byteToHex(byteArray[i]));
	    }
	    return hexStringBuffer.toString();
	}
	
	public static String encodeHexStringLen(byte[] byteArray, int len) {
	    StringBuffer hexStringBuffer = new StringBuffer();
	    for (int i = 0; i < len; i++) {
	        hexStringBuffer.append(byteToHex(byteArray[i]));
	    }
	    return hexStringBuffer.toString();
	}
	
	public static String byteToHex(byte num) {
	    char[] hexDigits = new char[3];
	    hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
	    hexDigits[1] = Character.forDigit((num & 0xF), 16);
	    hexDigits[2] = ' ';
	    return new String(hexDigits);
	}
	
	public static String HexString(byte[] byteArray) {
	    StringBuffer hexStringBuffer = new StringBuffer();
	    for (int i = 0; i < byteArray.length; i++) {
	        hexStringBuffer.append(byteHex(byteArray[i]));
	    }
	    return hexStringBuffer.toString();
	}
	
	
	public static String byteHex(byte num) {
	    char[] hexDigits = new char[2];
	    hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
	    hexDigits[1] = Character.forDigit((num & 0xF), 16);	    
	    return new String(hexDigits);
	}
	
	public static String VmaID_String(byte dat1, byte dat2, byte dat3) {
		long tDevice=0;		
		long tArea=0;
		long tNumber=0;
		long tTmp=0;
	    String hexValue="";
	    String sID="";
	    tDevice = 0;

	    hexValue=String.format("%02X",dat1) + String.format("%02X",dat2) + String.format("%02X",dat3);
	    tDevice =  Long.parseLong(hexValue, 16);
	    

	    tArea = ((tDevice & 0x00f00000)>>20);
	    tNumber = ((tDevice & 0x000fc000)>>14);
	    tTmp = (tDevice & 0x00003fff);
	    
//	    
//	    System.out.println("#0 HEX: " + hexValue + String.format(" : %08X",tDevice));
//	    
//	    sID = String.format("%d", tArea);
//	    sID = sID + String.format(" - %02d", tNumber);		
//	    sID = sID + String.format("- %05d", tTmp);
//	    
//	    System.out.println("#1 VmaID_String:"+sID);
	   
	    sID = String.format("%d", tArea);
	    sID = sID + String.format("%02d", tNumber);		
	    sID = sID + String.format("%05d", tTmp);
	    
//	  System.out.println("#2 VmaID_String:"+sID);
	    
	    return sID;
	}

	public static byte String2hex(char c) {
		switch (c) {
			case '0':
				return 0x00;
			case '1':
				return 0x01;
			case '2':
				return 0x2;
			case '3':
				return 0x3;
			case '4':
				return 0x4;
			case '5':
				return 0x5;
			case '6':
				return 0x6;
			case '7':
				return 0x7;
			case '8':
				return 0x8;
			case '9':
				return 0x9;
			case 'A':
			case 'a':
				return 0xa;
			case 'B':
			case 'b':
				return 0xb;
			case 'C':
			case 'c':
				return 0xc;
			case 'D':
			case 'd':
				return 0xd;
			case 'E':
			case 'e':
				return 0xe;
			case 'F':
			case 'f':
				return 0xf;
			default:
				return -1;
		}
	}
	
	public static String val_BinaryString(byte dat) {
		
	    String sVal = "";
	    
	    if((dat & 0x80) == 0x80){sVal="1";}else{sVal="0";}
	    if((dat & 0x40) == 0x40){sVal=sVal+"1";}else{sVal=sVal+"0";}
	    if((dat & 0x20) == 0x20){sVal=sVal+"1";}else{sVal=sVal+"0";}
	    if((dat & 0x10) == 0x10){sVal=sVal+"1";}else{sVal=sVal+"0";}
	    if((dat & 0x08) == 0x08){sVal=sVal+"1";}else{sVal=sVal+"0";}
	    if((dat & 0x04) == 0x04){sVal=sVal+"1";}else{sVal=sVal+"0";}
	    if((dat & 0x02) == 0x02){sVal=sVal+"1";}else{sVal=sVal+"0";}
	    if((dat & 0x01) == 0x01){sVal=sVal+"1";}else{sVal=sVal+"0";}
	    	    
	    return sVal;
	}
	
    public static byte hexStringToByte(String hexString) {
 	    int firstDigit = toDigit(hexString.charAt(0));
 	    int secondDigit = toDigit(hexString.charAt(1));
 	    return (byte) ((firstDigit << 4) + secondDigit);
 	}
     
     private static int toDigit(char hexChar) {
 	    int digit = Character.digit(hexChar, 16);
 	    if(digit == -1) {
 	        throw new IllegalArgumentException(
 	          "Invalid Hexadecimal Character: "+ hexChar);
 	    }
 	    return digit;
 	}
     
     public static byte asciitohex(String hex) {
    	   byte iDat = 0x00;
    	   if(hex.equals("0")) {
    		   iDat = 0x00;
    	   }else if (hex.equals("1")) {
    		   iDat = 0x01;
    	   }else if (hex.equals("2")) {
    		   iDat = 0x02;
    	   }else if (hex.equals("3")) {
    		   iDat = 0x03;
    	   }else if (hex.equals("4")) {
    		   iDat = 0x04;
    	   }else if (hex.equals("5")) {
    		   iDat = 0x05;
    	   }else if (hex.equals("6")) {
    		   iDat = 0x06;
    	   }else if (hex.equals("7")) {
    		   iDat = 0x07;
    	   }else if (hex.equals("8")) {
    		   iDat = 0x08;
    	   }else if (hex.equals("9")) {
    		   iDat = 0x09;
    	   }else if (hex.equals("a")) {
    		   iDat = 0x0A;
    	   }else if (hex.equals("b")) {
    		   iDat = 0x0B;
    	   }else if (hex.equals("c")) {
    		   iDat = 0x0C;
    	   }else if (hex.equals("d")) {
    		   iDat = 0x0D;
    	   }else if (hex.equals("e")) {
    		   iDat = 0x0E;
    	   }else if (hex.equals("f")) {
    		   iDat = 0x0F;
    	   }else if (hex.equals("A")) {
    		   iDat = 0x0A;
    	   }else if (hex.equals("B")) {
    		   iDat = 0x0B;
    	   }else if (hex.equals("C")) {
    		   iDat = 0x0C;
    	   }else if (hex.equals("D")) {
    		   iDat = 0x0D;
    	   }else if (hex.equals("E")) {
    		   iDat = 0x0E;
    	   }else if (hex.equals("F")) {
    		   iDat = 0x0F;
    	   }
           
    	   return iDat;
    	}
     
     public static String desTohex(String idkapal) {
         try {
             String b1, b2, b3, b4;
             int    sub1, sub2, sub3;
             int kpId;
             b1=idkapal.substring(0, 1);
             b2=idkapal.substring(1, 3);
             b3=idkapal.substring(3, 8);
             sub1 = Integer.parseInt(b1,10);
             sub2 = Integer.parseInt(b2,10);
             sub3 = Integer.parseInt(b3,10);
             sub1 = sub1 * 0x100000;
             sub2 = sub2 * 0x4000;
             kpId = sub1 + sub2 + sub3;
                                       
             String tmpStrKp = Integer.toHexString(kpId);
             return tmpStrKp.toUpperCase();
         }catch (Exception e){
             return "000000";
         }
     }

     public static String bleNameToID(String name){
		 int i = 0;
		 byte[] result = new byte[3];

		 for(int index=0;index<3;index++) {
			 byte tmp1 = String2hex(name.charAt(i++));
			 byte tmp2 = String2hex(name.charAt(i++));
			 result[index] =(byte) ((tmp1 << 4) | tmp2);

		 }
		 System.out.println("data "+ result.length);
		 return NcsUtill.VmaID_String(result[0], result[1], result[2]);
	 }
     
     public static String tanggal_realtime() {
    	   DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	   Date dateobj = new Date();
    	   return df.format(dateobj);
       }
	
}
