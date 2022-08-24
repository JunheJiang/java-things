package com.ljp.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ljp.netty.common.SocketModel;

public class KafkaSimulator {
	public static Logger  logger = Logger.getLogger(KafkaSimulator.class);
	
	public static void main(String[] args) {
		SocketModel socketModel = new SocketModel();
        socketModel.setType("websocketPush");
        List<String> listMessage = new ArrayList<String>();	
        
        //消息内容参数list中，第一个参数我们指代为将要推送的应用名，第二个参数指代为将要推送到的用户id或卡号，第三个参数是消息的内容。
        listMessage.add("webB");
        listMessage.add("A0001");
        listMessage.add("this is the test content");
        
        socketModel.setMessage(listMessage);     
        
        File file = new File("D:/push_websocket_topic.txt");
//        File file =new File("D:/push_websocket_topic_sub02.txt");
        
        
        //这里是以写对象的形式，将对象序列化成为字符流，写入文件，当要读取回信息的时候，只需要反序列化即可。
        //如果是以字符串形式写入到文件的，只需要在读取文件的时候进行字符串读取即可。
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOutputStream);
            objOut.writeObject(socketModel);
            objOut.flush();
            objOut.close();
            logger.info("write object success!");
        } catch (IOException e) {
            logger.error("write object failed");
            e.printStackTrace();
        }
        
        
	}
}
