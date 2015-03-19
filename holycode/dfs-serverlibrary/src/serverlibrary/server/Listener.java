package serverlibrary.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;
import java.nio.channels.*;
import java.util.*;


public class Listener implements Runnable {

	private final serverlibrary.task.TaskFactory taskFactory;
	private Logger log;
	private int port;


	// create logger, add formatter
	private void createLog() throws IOException {
		FileHandler hand = new FileHandler("log/server.log");
		log = Logger.getLogger("server");
		log.addHandler(hand);
		log.setLevel(Level.ALL);
		SimpleFormatter formatter = new SimpleFormatter();
		hand.setFormatter(formatter);
	}

	private void setPort(int port) {
		this.port = port;
	}

	public Listener(int port, serverlibrary.task.TaskFactory taskFactory) {
		this.taskFactory = taskFactory;
//		try {
//			createLog();
//		} catch (IOException e) {
//			System.err.println("ERROR: can't create log");
//			//e.printStackTrace();
//		}
		log = Logger.getLogger("server");

		setPort(port);
		//new Thread(this).start();
	}

	public void run() {
		log.info("Starting server on port " + port + "  ...");
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);

			// Get the Socket connected to this channel, and bind it
			// to the listening port
			ServerSocket ss = ssc.socket();
			InetSocketAddress isa = new InetSocketAddress(port);
			ss.bind(isa);

			// Create a new Selector for selecting
			Selector selector = Selector.open();

			// Register the ServerSocketChannel, so we can
			// listen for incoming connections
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			log.info("Listening on port " + port);

			ExecutorService threadPool = Executors.newCachedThreadPool();

			while (selector.select() > 0) {
				// Get the keys corresponding to the activity
				// that has been detected, and process them
				// one by one
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
 
				while (it.hasNext()) {
					// Get a key representing one of bits of I/O
					// activity
					SelectionKey key = (SelectionKey) it.next();

					// The key indexes into the selector so you
					// can retrieve the socket that's ready for I/O
					ServerSocketChannel nextReady =
					    (ServerSocketChannel)key.channel();
					// Accept the date request and send back the date string
					SocketChannel socketChannel = nextReady.accept();


					if(Defender.checkIP(socketChannel.socket().getInetAddress().toString())) {
						// submit new task to executor
						threadPool.submit(new Connector(socketChannel, taskFactory));
						log.info("Got connection from " + socketChannel.socket());
					} else {
						log.info("Try to connect from bad IP " + socketChannel.socket());
						socketChannel.close();
					}
				}

				// We remove the selected keys, because we've dealt
				// with them.
				keys.clear();
			}

		} catch (IOException ex) {
			log.log(Level.SEVERE, "Can't open serverSocketChannal", ex);
		}

	}

}
