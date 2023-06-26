import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TCPClient {
    private static final int PORT = 12000;
    private static final String SERVER_IP = "127.0.0.1";

    private static final byte[] Key = {
            (byte) 0x2b, (byte) 0x7e, (byte) 0x15, (byte) 0x16,
            (byte) 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6,
            (byte) 0xab, (byte) 0xf7, (byte) 0x15, (byte) 0x88,
            (byte) 0x09, (byte) 0xcf, (byte) 0x4f, (byte) 0x3c
    };
    public static void main(String[] args) {
        BufferedReader inputReader = null;
        BufferedWriter serverwriter = null;
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("서버에 연결되었습니다.");

            inputReader = new BufferedReader(new InputStreamReader(System.in));
            serverwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            SendClass Sender = new SendClass();
            AudioClass AudioSender = new AudioClass();

            String input;

            boolean exit = true;
            while (socket.isConnected()) {
                System.out.print("원하는 기능을 선택하세요 (1: 메시지, 2: 파일 전송, 3: 오디오 파일 전송, q: 종료): ");

                String choice = inputReader.readLine();

                switch (choice) {
                    case "1":
                        // 텍스트 채팅 기능
                        System.out.print("메세지를 입력하세요 : ");
                        Sender.TextSend(choice, serverwriter);
                        input = inputReader.readLine();
                        String Encmessage = FileEncryption.encryptString(input,Key);
                        System.out.println("암호화 된 메세지 : " + Encmessage);
                        Sender.TextSend(Encmessage, serverwriter);
                        break;
                    case "2":
                        // 파일 전송 기능
                        System.out.print("파일 경로를 입력하세요 : ");
                        Sender.TextSend(choice, serverwriter);
                        File file = new File(inputReader.readLine());

                        File Encfile = FileEncryption.encryptFile(file, Key);
                        Sender.Filesend(Encfile, socket, serverwriter);

                        socket = new Socket(SERVER_IP, PORT);
                        serverwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        break;
                    case "3":
                        // 파일 전송 후 재생 기능
                        System.out.print("파일 경로를 입력하세요 : ");
                        Sender.TextSend(choice, serverwriter);
                        File audiofile = new File(inputReader.readLine());

                        File AudioEncfile = FileEncryption.encryptFile(audiofile, Key);
                        Sender.Filesend(AudioEncfile, socket, serverwriter);
                        AudioSender.AudioPlay(audiofile);

                        socket = new Socket(SERVER_IP, PORT);
                        serverwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        break;
                    case "q":
                        System.out.println("프로그램을 종료합니다.");
                        Sender.TextSend(choice, serverwriter);
                        return;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
                }
            }
            System.out.println("서버와의 연결이 종료되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException |InvalidKeyException EncError) {
            throw new RuntimeException(EncError);
        } finally {
            try{
                inputReader.close();
                serverwriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}