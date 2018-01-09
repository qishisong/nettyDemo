package NettyMessageTest;

import lombok.Data;

/**
 * @author qiss
 * @create 2017-12-27 11:37
 **/
@Data
public final class NettyMessage {
    private Header header;
    private Object body;

    @Override
    public String toString(){
        return "NettyMessage [header="+header+"]";
    }
}
