package sample;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.w3c.dom.ls.LSOutput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    Socket socket;
    @FXML
    TextField textField;
    @FXML
    TextArea textArea;
    @FXML
    TextArea onlineUsers;
    @FXML
    private void send(){
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String text = textField.getText();
            out.writeUTF(text);
            textField.clear(); // Очищаем поле ввода сообщения
            textField.requestFocus(); // Возвращаем фокусировку на поле ввода
            textArea.appendText("Вы: "+text+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void connect(){
        try {
            socket = new Socket("localhost",8188);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        String response = "";
                        ArrayList<String> usersName = new ArrayList<String>();
                        try {
                            Object object = ois.readObject();
                            if(object.getClass().equals(usersName.getClass())){
                                usersName = ((ArrayList<String>) object);
                                System.out.println(usersName);
                                onlineUsers.clear(); // Очищает TextArea
                                for (String userName:usersName) {
                                    onlineUsers.appendText(userName+"\n");
                                }
                            }else if (object.getClass().equals(response.getClass())){
                                response = object.toString();
                                textArea.appendText(response+"\n");
                            }else{
                                textArea.appendText("Произошла ошибка");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
