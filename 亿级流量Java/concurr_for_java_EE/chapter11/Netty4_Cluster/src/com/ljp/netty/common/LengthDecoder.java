package com.ljp.netty.common;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class LengthDecoder extends LengthFieldBasedFrameDecoder{
	 
    public LengthDecoder(int maxFrameLength, int lengthFieldOffset,
            int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
                initialBytesToStrip);
    }
     
}
