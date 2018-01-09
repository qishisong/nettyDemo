package NettyMessageTest;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiss
 * @create 2017-12-27 11:24
 **/
@Data
public final class Header {
    private int crcCode = 0xabef0101;
    private int length;
    private long sessionID;
    private byte type;  //message type
    private byte pripority;
    private Map<String,Object> attachment = new HashMap<String, Object>();

    @Override
    public String toString(){
        return "Header [crcCode="+crcCode+",length="+length+",sessionID="
                +sessionID+",type="+",priority="+pripority+",attachment="+attachment+"]";
    }
}
