import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 6000);
            System.out.println("서버에 연결되었습니다.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                System.out.print("메시지를 입력하세요 (1: 숫자 메시지, 2: 파일 전송, 3: 오디오 파일 전송, q: 종료): ");
                String message = reader.readLine();

                if (message.equals("q")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }

                writer.write(message);
                writer.newLine();
                writer.flush();

                if (message.equals("1")) {
                    System.out.print("메시지를 입력하세요: ");
                    String response = reader.readLine();
                    writer.write(response);
                    writer.newLine();
                    writer.flush();
                } else if (message.equals("2")) {
                    System.out.print("파일 경로를 입력하세요: ");
                    String filePath = reader.readLine();
                    File file = new File(filePath);
                    if (file.exists()) {
                        // 파일 전송 코드 추가
                    } else {
                        System.out.println("파일이 존재하지 않습니다.");
                    }
                } else if (message.equals("3")) {
                    System.out.print("오디오 파일 경로를 입력하세요: ");
                    String audioFilePath = reader.readLine();
                    File audioFile = new File(audioFilePath);
                    if (audioFile.exists()) {
                        // 오디오 파일 전송 및 재생 코드 추가
                    } else {
                        System.out.println("오디오 파일이 존재하지 않습니다.");
                    }
                }
            }

            reader.close();
            writer.close();
            serverReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}