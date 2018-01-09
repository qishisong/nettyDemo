package NettyMessageTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

import java.util.List;
import java.util.Map;

/**
 * @author qiss
 * @create 2017-12-27 11:39
 **/
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    NettyMarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder(){
        this.marshallingEncoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if(msg == null || msg.getHeader() == null){
            throw new Exception("The encode message is null");
        }
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode())
                .writeInt(msg.getHeader().getLength())
                .writeLong(msg.getHeader().getSessionID())
                .writeByte(msg.getHeader().getType())
                .writeByte(msg.getHeader().getPripority())
                .writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length).writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(ctx,value,sendBuf);
        }
        key = null;
        keyArray = null;
        value = null;
        if(msg.getBody() != null){
            marshallingEncoder.encode(ctx,msg.getBody(),sendBuf);
        }
        int re = sendBuf.readableBytes();
        sendBuf.setInt(4,re);
        out.add(sendBuf);

    }
}
