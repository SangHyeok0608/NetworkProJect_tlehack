import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("서버가 시작되었습니다.");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트가 연결되었습니다.");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                while (true) {
                    String message = reader.readLine();

                    if (message.equals("q")) {
                        System.out.println("클라이언트와의 연결을 종료합니다.");
                        break;
                    }

                    if (message.equals("1")) {
                        // 메시지 전송
                        String receivedMessage = serverReader.readLine();
                        System.out.println("클라이언트에서 받은 메시지: " + receivedMessage);
                    } else if (message.equals("2")) {


                        // 파일 전송 처리 코드 추가
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String CopyPath = "Server_sample" + File.separator + "Copyfile3.jpg";
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(CopyPath));
                        String line;
                        int data= 0;
                        char []buffer = new char[1024];
                        while ((data = bufferedReader.read(buffer)) != -1) {
                            bufferedWriter.write(buffer, 0, data);
                        }
                        bufferedWriter.close();
                        serverSocket.close();


                        System.out.println("파일 수신이 완료되었습니다.");




                    } else if (message.equals("3")) {
                        // 오디오 파일 전송 및 재생 코드 추가
                    }
                }
                reader.close();
                writer.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}