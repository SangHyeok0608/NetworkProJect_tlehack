import java.io.*;
import java.net.*;

public class testserver {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(21000);
            System.out.println("서버가 시작되었습니다.");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트가 연결되었습니다.");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream outputStream = socket.getOutputStream();

                while (true) {
                    String message = reader.readLine();

                    if (message.equals("q")) {
                        System.out.println("클라이언트와의 연결을 종료합니다.");
                        break;
                    }

                    if (message.equals("1")) {
                        // 메시지 전송
                        String receivedMessage = reader.readLine();
                        System.out.println("서버에서 받은 메시지: " + receivedMessage);
                    } else if (message.equals("2")) {
                        // 파일 전송 처리
                        receiveFile(socket);
                    } else if (message.equals("3")) {
                        // 오디오 파일 전송 및 재생 코드 추가
                    }
                }
                reader.close();
                outputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(Socket socket) throws IOException {
        byte[] buffer = new byte[4096];
        InputStream inputStream = socket.getInputStream();

        // 파일 이름 수신
        ByteArrayOutputStream fileNameStream = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileNameStream.write(buffer, 0, bytesRead);
             if (bytesRead < buffer.length) {
                 break;
             }
        }
        String fileName = new String(fileNameStream.toByteArray());
        System.out.println("파일을 수신받습니다: " + fileName);

        // 파일 데이터 수신
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            bufferedOutputStream.write(buffer, 0, bytesRead);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        fileOutputStream.close();
        System.out.println("파일 수신이 완료되었습니다.");
    }
}
