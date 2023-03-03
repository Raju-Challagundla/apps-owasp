import java.io.*;
import java.net.*;

public class ProxyServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Proxy server listening on port " + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            Socket serverSocket = new Socket("example.com", 80);
            DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());
            out.writeBytes(inputLine);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String serverInputLine;
            while ((serverInputLine = serverIn.readLine()) != null) {
                System.out.println(serverInputLine);
                DataOutputStream clientOut = new DataOutputStream(clientSocket.getOutputStream());
                clientOut.writeBytes(serverInputLine);
            }
            // Add custom header to response
            clientSocket.getOutputStream().write("X-Proxy-Header: my-custom-header\r\n".getBytes());
            in.close();
            out.close();
            serverIn.close();
            clientSocket.close();
            serverSocket.close();
        }
    }
}
