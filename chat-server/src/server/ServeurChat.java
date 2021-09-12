package server;

/* to test use telnet clients all of them */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServeurChat extends Thread {

	private boolean isActive = true;
	private int nombreClients = 0;
	protected List<Conversation> clients = new ArrayList<Conversation>();

	public static void main(String[] args) throws IOException {

		System.out.println("demarrage du serveur");
		new ServeurChat().start();
		System.out.println("suite de l\'application , les interfaces graphiques par exemple");
	}

	@Override
	public void run() {

		/* Creation de l'objet server socket avec numero du port : 1234 */

		try {

			ServerSocket serverSocket = new ServerSocket(1234);
			while (isActive) {

				Socket socket = serverSocket.accept();
				++nombreClients;
				Conversation conversation = new Conversation(socket, nombreClients);
				clients.add(conversation);
				conversation.start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class Conversation extends Thread {

		protected Socket socketClient;
		protected int clientCode;

		public Conversation(Socket socket, int code) {

			this.socketClient = socket;
			this.clientCode = code;
		}

		public void broadcastMessage(String message, Socket socket, int numCli) {
			try {

				for (Conversation client : clients) {
					if (client.socketClient != socket) {
						if (client.clientCode == numCli ||  numCli == -1) {
							PrintWriter pw = new PrintWriter(client.socketClient.getOutputStream(), true);
							pw.println(message);

						}

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void run() {

			try {

				// input
				InputStream is = socketClient.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				// output

				OutputStream os = socketClient.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				String IpAdress = socketClient.getRemoteSocketAddress().toString();
				pw.println("Bienvenue , vous etes le client numero " + clientCode);
				System.out.println("connexion du client : " + IpAdress);
				
				while (true) {

					String requete = br.readLine();

					if (requete.contains("=>")) {
						String[] requestParams = requete.split("=>");
						if (requestParams.length == 2) {
							String message = requestParams[1];
							int numeroClient = Integer.parseInt(requestParams[0]);
							broadcastMessage(message, socketClient, numeroClient);

						}

					} else if(requete != null) {
						broadcastMessage(requete,socketClient,-1);

					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
