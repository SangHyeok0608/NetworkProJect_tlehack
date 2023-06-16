# NetworkProJect_tlehack

java로 tcp를 이용한 통신을 구현하려고 합니다. 

TCPClient에서 숫자 1을 입력하면 TCPClient에서 TCPServer로 메세지를 보낸다는 뜻이다. 그리고 TCPClient에서 메세지를 입력하여 보내면, TCPServer는 받은 메세지를 출력한다.

TCPClient에서 숫자 2을 입력하면 TCPServer으로 파일을 보낸다는 뜻이다. 그리고 TCPClient에서 파일 경로명을 입력하면 파일 경로명을 통해 파일을 읽어오고 TCPServer로 데이터를 보낸다. TCPServer는 받은 데이터를 Server_sample 폴더에 저장한다. 

TCPClient에서 숫자 3을 입력하면 TCPServer으로 오디오파일을 보낸다는 뜻이다. 그리고 TCPClient에서 파일 경로명을 입력하면 파일 경로명을 통해 파일을 읽어오고 TCPServer로 데이터를 보낸다. TCPServer는 받은 데이터를 Server_sample 폴더에 저장하고 받은 오디오파일을 재생한다. 
