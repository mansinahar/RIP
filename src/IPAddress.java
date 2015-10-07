/*
 *@author Mansi Nahar 
 */
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
	
	/*
	 * given a 32 bit binary string, converts it into a IP address
	 * @param binaryIP
	 * 			String representation of the binary IP address
	 */
	public InetAddress convertBinarytoIP(String binaryIP) throws Exception {
		String ipAddr = "";
		for(int i = 0; i <= 16; i = i+8) {
			ipAddr += Integer.parseInt(binaryIP.substring(i, i+8),2);
			ipAddr += ".";
		}
		ipAddr += Integer.parseInt(binaryIP.substring(24),2);
		
		return InetAddress.getByName(ipAddr);
	}
	
	/*
	 * Converts the given IP address into it's binary string
	 * 
	 */
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
	
	
	/*
	 * Converts and returns the IP address corresponding to the given numerical subnet mask
	 * @param mask
	 * 			Subnet mask in the numerical form
	 */
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
	
	/*
	 * Returns the network prefix given an IP Address and it's subnet mask.
	 * @param ip
	 * 			IP Address in it's string format
	 * @param mask
	 * 			Subnet mask of that IP address
	 */
	public InetAddress getNetworkPrefix(String ip, int mask)throws Exception {
		
		String ipAddr = convertIPtoBinary(ip);
        String maskAddr = convertMask(mask).getHostAddress();
        
        BigInteger ipBigInt, maskBigInt, resultBigInt;
        ipBigInt = new BigInteger(ipAddr, 2);
        maskBigInt = new BigInteger(maskAddr, 2);
        resultBigInt = ipBigInt.and(maskBigInt);
        
        
        ipAddr = resultBigInt.toString(2);
        
        return convertBinarytoIP(ipAddr);
        
        
	}
	
}
