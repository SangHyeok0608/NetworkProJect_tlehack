import org.w3c.dom.Text;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

public class SendClass {

    private static final String Errormessage = "Error_message";
    private static final String Filemessage = "File_message";
    public SendClass() {}

    public void TextSend(String message, BufferedWriter serverWriter) {
        try {
            serverWriter.write(message);
            serverWriter.newLine();
            serverWriter.flush();

        } catch (IOException TextError) {
            TextError.printStackTrace();
        }
    }

    public void Filesend(File file, Socket socket, BufferedWriter serverWriter) throws IOException{

        if(file.exists()){
            TextSend(Filemessage,serverWriter);
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream inputstream = new BufferedInputStream(fileInputStream);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // 파일 이름 전송
            outputStream.writeUTF(file.getName());

            // 파일 데이터 전송
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputstream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            System.out.println("파일 전송이 완료되었습니다.");

            outputStream.close();
            inputstream.close();
            fileInputStream.close();
        }
        else {
            System.out.println("파일이 존재하지 않습니다.");
            TextSend(Errormessage,serverWriter);
        }
    }
}
