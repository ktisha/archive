package adminView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class AdminView {
	static Socket socket = null;

	static class WriteThread extends Thread {
		PrintWriter out;

		public WriteThread(PrintWriter out) {
			this.out = out;
		}

		public void run() {
			String str;
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				while ((str = stdIn.readLine()) != null)
					out.println(str);

			} catch (IOException ex) {
				System.err.println(ex);
				System.exit(-1);
			} finally {
				try {
					socket.close();
					stdIn.close();
					out.close();
				} catch (IOException ex2) {
					System.err.println(ex2);
					System.exit(-1);
				}
			}

		}
	}

	static class ReadThread extends Thread {
		BufferedReader in;

		ReadThread(BufferedReader in) {
			this.in = in;
		}

		public void run() {
			String str;
			try {
				while ((str = in.readLine()) != null) {
					System.out.println("it: " + str);
				}
				in.close();
			} catch (IOException ex) {
				System.err.println(ex);
				System.exit(-1);
			}

		}
	}

	static ReadThread runClient() throws IOException {
		System.out.println("Connect to 127.0.0.1:8000 ...");

		PrintWriter out = null;
		BufferedReader in = null;

		try {
			socket = new Socket("192.168.211.26", 8000);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: 127.0.0.1");
			return null;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: 127.0.0.1:8000");
			prt("Running server ...");
			return null;
		}
		ReadThread readTh = new ReadThread(in);
		WriteThread writeTh = new WriteThread(out);
		readTh.start();
		writeTh.start();
		return readTh;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
		ReadThread readTh = null;

		readTh = runClient();
		if (readTh != null)
			prt("Client started");
		else
			System.exit(-1);
		

		readTh.join();
		prt("Closed connection");
	}
	
	static void prt(String str) {
		System.out.println(str);
	}
}
