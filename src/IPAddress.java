import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Arrays;

public class IPAddress {
	InetAddress address;
	int mask;
	InetAddress networkPrefix;
	InetAddress maskAddress;
	
	public IPAddress(InetAddress address, int mask) throws Exception {
		this.address = address;
		this.mask = mask;
		this.networkPrefix = getNetworkPrefix(address.getHostAddress(), mask);
		this.maskAddress = convertMask(mask);
	}
	
	public InetAddress convertBinarytoIP(String binaryIP) throws Exception {
		String ipAddr = "";
		for(int i = 0; i <= 16; i = i+8) {
			ipAddr += Integer.parseInt(binaryIP.substring(i, i+8),2);
			ipAddr += ".";
		}
		ipAddr += Integer.parseInt(binaryIP.substring(24),2);
		
		return InetAddress.getByName(ipAddr);
	}
	
	public String convertIPtoBinary(String ip) {
		// Convert ip address to it's binary representation
				StringBuilder bStringBuilder = new StringBuilder();
		        String ipParts[] = ip.split("\\.");

		        for (String ipPart : ipParts) {

		            String binString = Integer.toBinaryString(Integer.parseInt(ipPart));
		            int length = 8 - binString.length();
		            char[] padArray = new char[length];
		            Arrays.fill(padArray, '0');
		            bStringBuilder.append(padArray).append(binString);
		        }
		        return bStringBuilder.toString();
	}
	
	
	public InetAddress convertMask(int mask) throws Exception {
		String maskAddr = "";
        
        for(int i = 0; i < mask; ++i) {
        	maskAddr += "1";
        }
        
        for(int i = mask; i < 32; ++i) {
        	maskAddr += "0";
        }
        
        return convertBinarytoIP(maskAddr);
	}
	
	
	public InetAddress getNetworkPrefix(String ip, int mask)throws Exception {
		
		String ipAddr = convertIPtoBinary(ip);
        String maskAddr = "";
        
        for(int i = 0; i < mask; ++i) {
        	maskAddr += "1";
        }
        
        for(int i = mask; i < 32; ++i) {
        	maskAddr += "0";
        }
        
        BigInteger ipBigInt, maskBigInt, resultBigInt;
        ipBigInt = new BigInteger(ipAddr, 2);
        maskBigInt = new BigInteger(maskAddr, 2);
        resultBigInt = ipBigInt.and(maskBigInt);
        
        
        ipAddr = resultBigInt.toString(2);
        
        return convertBinarytoIP(ipAddr);
        
        
	}
	
	public static void main(String args[]) throws Exception {
		
		// Testing ipAddress class
		IPAddress ipAddr = new IPAddress(InetAddress.getByName("129.23.41.15"), 24);
		System.out.println("Address: " + ipAddr.address.getHostAddress());
		System.out.println("Mask: " + ipAddr.mask);
		System.out.println("Mask Address: " + ipAddr.maskAddress.getHostAddress());
		System.out.println("Network Prefix: " + ipAddr.networkPrefix.getHostAddress());
	}
	
}
