package livekit.org.webrtc.video;

import androidx.annotation.Nullable;

import livekit.org.webrtc.EglBase;
import livekit.org.webrtc.SoftwareVideoDecoderFactory;
import livekit.org.webrtc.VideoCodecInfo;
import livekit.org.webrtc.VideoDecoder;
import livekit.org.webrtc.VideoDecoderFactory;
import livekit.org.webrtc.WrappedVideoDecoderFactory;

import java.util.ArrayList;
import java.util.List;

public class CustomVideoDecoderFactory implements VideoDecoderFactory {
    private SoftwareVideoDecoderFactory softwareVideoDecoderFactory = new SoftwareVideoDecoderFactory();
    private WrappedVideoDecoderFactory wrappedVideoDecoderFactory;
    private boolean forceSWCodec  = false;

    private List<String> forceSWCodecs = new ArrayList<>();

    public  CustomVideoDecoderFactory(EglBase.Context sharedContext) {
        this.wrappedVideoDecoderFactory = new WrappedVideoDecoderFactory(sharedContext);
    }

    public void setForceSWCodec(boolean forceSWCodec) {
        this.forceSWCodec = forceSWCodec;
    }

    public void setForceSWCodecList(List<String> forceSWCodecs) {
        this.forceSWCodecs = forceSWCodecs;
    }

    @Nullable
    @Override
    public VideoDecoder createDecoder(VideoCodecInfo videoCodecInfo) {
        if(forceSWCodec) {
            return softwareVideoDecoderFactory.createDecoder(videoCodecInfo);
        }
        if(!forceSWCodecs.isEmpty()) {
            if(forceSWCodecs.contains(videoCodecInfo.name)) {
                return softwareVideoDecoderFactory.createDecoder(videoCodecInfo);
            }
        }
        return wrappedVideoDecoderFactory.createDecoder(videoCodecInfo);
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        if(forceSWCodec && forceSWCodecs.isEmpty()) {
            return softwareVideoDecoderFactory.getSupportedCodecs();
        }
        return wrappedVideoDecoderFactory.getSupportedCodecs();
    }
}
