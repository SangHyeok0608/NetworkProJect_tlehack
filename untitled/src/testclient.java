import java.io.*;
import java.net.*;

public class testclient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 21000);
            System.out.println("서버에 연결되었습니다.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            while (true) {
                System.out.print("메시지를 입력하세요 (1: 숫자 메시지, 2: 파일 전송, 3: 오디오 파일 전송, q: 종료): ");
                String message = reader.readLine();

                if (message.equals("q")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }

                outputStream.write(message.getBytes());
                outputStream.flush();

                switch (message) {
                    case "1":
                        System.out.print("메시지를 입력하세요: ");
                        String response = reader.readLine();
                        outputStream.write(response.getBytes());
                        outputStream.flush();
                        break;
                    case "2":
                        System.out.print("파일 경로를 입력하세요: ");
                        String filePath = reader.readLine();
                        File file = new File(filePath);
                        if (file.exists()) {
                            sendFile(file, outputStream);
                        } else {
                            System.out.println("파일이 존재하지 않습니다.");
                        }
                        break;
                    case "3":
                        System.out.print("오디오 파일 경로를 입력하세요: ");
                        String audioFilePath = reader.readLine();
                        File audioFile = new File(audioFilePath);
                        if (audioFile.exists()) {
                            sendFile(audioFile, outputStream);
                        } else {
                            System.out.println("오디오 파일이 존재하지 않습니다.");
                        }
                        break;
                }
            }

            reader.close();
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(File file, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedFileInputStream = new BufferedInputStream(fileInputStream);

        // 파일 이름 전송
        String fileName = file.getName();
        byte[] fileNameBytes = fileName.getBytes();
        outputStream.write(fileNameBytes, 0, fileNameBytes.length);
        outputStream.flush();

        // 파일 데이터 전송
        int bytesRead;
        while ((bytesRead = bufferedFileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            outputStream.flush();
        }

        bufferedFileInputStream.close();
        fileInputStream.close();
        System.out.println("파일 전송이 완료되었습니다.");
    }
}
