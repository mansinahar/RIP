import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logging {
	
	Path p;
	
	public Logging(String fileName) {
		p = Paths.get("./" + fileName + "LogFile.txt");
	}
	
	public void write(String msg) {
		msg = msg + "\n";
		byte data[] = msg.getBytes();
		try (OutputStream out = new BufferedOutputStream(
			      Files.newOutputStream(p, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
			      out.write(data, 0, data.length);
			    } catch (IOException x) {
			      System.err.println(x);
			    }
	}
	
	public static void main(String args[]) {
		Logging log = new Logging("test");
		log.write("testing\n");
		log.write("testing1\n");
		log.write("testing2\n");
	}
}
