package network.packets;

import java.io.Serializable;

public class RequestObject implements Serializable {
    private byte[] content;
    private int size;

    public RequestObject(byte[] content, int size){
        this.content = content;
        this.size = size;
    }
}
