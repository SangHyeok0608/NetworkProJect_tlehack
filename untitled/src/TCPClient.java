import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 21000);
            System.out.println("서버에 연결되었습니다.");

            BufferedReader textreader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter textwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream fileoutstream = socket.getOutputStream();

            while (true) {
                System.out.print("메시지를 입력하세요 (1: 숫자 메시지, 2: 파일 전송, 3: 오디오 파일 전송, q: 종료): ");
                String message = textreader.readLine();

                if (message.equals("q")) {
                    System.out.println("프로그램을 종료합니다.");
                    textwriter.write(message);
                    textwriter.newLine();
                    textwriter.flush();
                    break;
                }

                textwriter.write(message);
                textwriter.newLine();
                textwriter.flush();

                switch (message) {
                    case "1":
                        System.out.print("메시지를 입력하세요: ");
                        String response = textreader.readLine();
                        textwriter.write(response);
                        textwriter.newLine();
                        textwriter.flush();
                        break;
                    case "2":
                        System.out.print("파일 경로를 입력하세요: ");
                        String filePath = textreader.readLine();
                        File file = new File(filePath);
                        if (file.exists()) {
                            sendFile(file, fileoutstream);
                        } else {
                            System.out.println("파일이 존재하지 않습니다.");
                        }
                        break;
                    case "3":
                        System.out.print("오디오 파일 경로를 입력하세요: ");
                        String audioFilePath = textreader.readLine();
                        File audioFile = new File(audioFilePath);
                        if (audioFile.exists()) {
                            sendFile(audioFile, fileoutstream);

                            AudioInputStream audiostream = AudioSystem.getAudioInputStream(audioFile);
                            Clip clip = AudioSystem.getClip();

                            clip.open(audiostream);
                            clip.loop(1);
                            clip.start();
                        } else {
                            System.out.println("오디오 파일이 존재하지 않습니다.");
                        }
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendFile(File file, OutputStream outputStream) throws IOException {

        BufferedInputStream bufferedFileInputStream = new BufferedInputStream(new FileInputStream(file));
        try {

            // 파일 이름 전송
            byte[] fileNameBytes = file.getName().getBytes();
            outputStream.write(fileNameBytes.length);
            outputStream.write(fileNameBytes);

            // 파일 데이터 전송
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bufferedFileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();

            System.out.println("파일 전송이 완료되었습니다.");
        } finally {
            bufferedFileInputStream.close();
        }
    }
}