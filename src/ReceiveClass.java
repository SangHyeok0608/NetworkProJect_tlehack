import java.io.*;
import java.net.Socket;

public class ReceiveClass {

    private static final String Errormessage = "Error_message";
    private static final String Filemessage = "File_message";

    private static final String savePath = "Download" + File.separator;
    public ReceiveClass(){}

    public File FileReceive(Socket socket, BufferedReader serverReader) throws IOException{

        String Check = serverReader.readLine();
        if(Check.equals(Filemessage)){

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            // 파일 이름 수신
            String fileName = inputStream.readUTF();
            File file = new File(savePath + fileName);
            FileOutputStream fileoutputStream = new FileOutputStream(file);
            BufferedOutputStream outputStream = new BufferedOutputStream(fileoutputStream);

            // 파일 데이터 수신
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush(); //출력스트림을 마저 한번더 비우기

            //쓴 메모리 닫기
            outputStream.close();
            fileoutputStream.close();
            inputStream.close();
            System.out.println("\n파일 복사 완료 : " + fileName);
            return file;
        }
        else if(Check.equals(Errormessage)){
            throw new IOException("송신자의 파일 경로가 올바르지 않습니다.");
        }
        throw new IOException("파일 수신 에러");
    }
}
