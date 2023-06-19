import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(21000);
            System.out.println("서버가 시작되었습니다.");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트가 연결되었습니다.");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                InputStream fileinputstream = socket.getInputStream();

                while (true) {
                    String message = reader.readLine();

                    if (message.equals("q")) {
                        System.out.println("클라이언트와의 연결을 종료합니다.");
                        break;
                    }

                    switch (message) {
                        case "1":  // 메시지 전송
                            String receivedMessage = serverReader.readLine();
                            System.out.println("서버에서 받은 메시지: " + receivedMessage);
                            break;
                        case "2":  //파일 전송
                            receiveFile(fileinputstream);

                            break;
                        case "3":  //오디오 파일 전송 및 재생
                            File audioFile = receiveFile(fileinputstream);

                            AudioInputStream audiostream = AudioSystem.getAudioInputStream(audioFile);
                            Clip clip = AudioSystem.getClip();

                            clip.open(audiostream);
                            clip.loop(1);
                            clip.start();
                            break;
                    }
                }
                reader.close();
                writer.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static File receiveFile(InputStream inputStream) throws IOException {
        // 파일 이름 수신
        int fileNameLength = inputStream.read();
        byte[] fileNameBytes = new byte[fileNameLength];
        inputStream.read(fileNameBytes);
        String fileName = new String(fileNameBytes);
        File file = new File(fileName);

        // 파일 데이터 수신
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.flush();

            System.out.println("파일 복사 완료 : " + fileName);
            return file;
        } finally {
            fileOutputStream.close();
        }
    }
}

