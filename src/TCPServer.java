import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TCPServer {
    private static final int PORT = 9999;
    private static final String SERVER_IP = "127.0.0.1";
    private static String Errormessage = "Error_message";
    private static String Filemessage = "File_message";

    private static final byte[] Key = {
            (byte) 0x2b, (byte) 0x7e, (byte) 0x15, (byte) 0x16,
            (byte) 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6,
            (byte) 0xab, (byte) 0xf7, (byte) 0x15, (byte) 0x88,
            (byte) 0x09, (byte) 0xcf, (byte) 0x4f, (byte) 0x3c
    };
    public static void main(String[] args) {
        BufferedReader serverReader = null;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("서버가 시작되었습니다.");

            Socket socket = serverSocket.accept();
            System.out.println("클라이언트에 연결되었습니다.");

            //파일과 오디오 처리에 필요한 클래스 호출
            AudioClass AudioReceiver = new AudioClass();
            ReceiveClass Receiver = new ReceiveClass();

            //message : 채팅 및 파일 전송에 이용할 문자열 변수 / choice : 무엇을 받아올 것인지 판단할 변수
            String message;
            String choice;

            boolean exit = true;
            while (socket.isConnected()) {
                serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                choice = serverReader.readLine();

                if(choice == null)
                    continue;

                switch (choice) {
                    case "1":
                        // 텍스트 채팅 기능
                        message = serverReader.readLine();
                        System.out.println("\n클라이언트로부터 메세지 : " + message);
                        String Decmessage = FileEncryption.decryptString(message, Key);
                        System.out.println("복호화 된 메세지 : " + Decmessage);
                        break;
                    case "2":
                        // 파일 전송 기능
                        File file = Receiver.FileReceive(socket, serverReader);
                        FileEncryption.decryptFile(file, Key);
                        serverReader = null;
                        socket = serverSocket.accept();
                        break;
                    case "3":
                        // 파일 전송 후 재생 기능
                        File EncAudiofile = Receiver.FileReceive(socket, serverReader);
                        File DecAudiofile = FileEncryption.decryptFile(EncAudiofile, Key);
                        AudioReceiver.AudioPlay(DecAudiofile);

                        serverReader = null;
                        socket = serverSocket.accept();
                        break;
                    case "q":
                        System.out.println("프로그램을 종료합니다.");
                        exit = false;
                        return;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
                }
                if (choice.equals("q")) {
                    System.out.println("클라이언트가 종료를 요청했습니다.");
                    socket.close();
                    return;
                }
            }
            System.out.println("서버를 종료합니다.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | InvalidKeyException DecError) {
            throw new RuntimeException(DecError);
        } finally {
            try{
                serverReader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}