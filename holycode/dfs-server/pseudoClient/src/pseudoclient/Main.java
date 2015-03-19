package pseudoclient;

/**
 *
 * @author an
 */
public class Main {

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("PseudoClient for testing server and storages");
			System.out.println("Usage:");
			System.out.println("	-r	file for send as request");
			System.out.println("	-u	file for uploading");
			System.out.println("	-d	file for downloading");
			System.out.println("	-h	host for connect (default: localhost)");
			System.out.println("	-p	port of host for connect (default: 8000)");
		} else {

			Properties properies = new Properties();

			for (int i = 0; i < args.length; ++i) {
				if ("-r".equals(args[i])) {
					properies.request = args[++i];
				} else if ("-u".equals(args[i])) {
					properies.upload = args[++i];
				} else if ("-d".equals(args[i])) {
					properies.download = args[++i];
				} else if ("-h".equals(args[i])) {
					properies.host = args[++i];
				} else if ("-p".equals(args[i])) {
					properies.port = new Integer(args[++i]);
				}
			}

			if (properies.request == null) {
				System.err.println("Error: file for sending as request is required");
				System.exit(1);
			}

			new Thread(new PseudoClient(properies)).start();
		}
	}
}
