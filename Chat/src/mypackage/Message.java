package mypackage;

import java.io.Serializable;

/**
 * Created by DNS on 13.03.2017.
 */
public class Message implements Serializable {
    private final MessageType type;
    private final String data;

    public Message(MessageType messageType){
        this.type = messageType;
        data = null;
    }

    public Message(MessageType messageType, String data){
        this.type = messageType;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public String getData() {
        return data;
    }




}
