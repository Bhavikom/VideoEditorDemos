package zippler.cn.xs.util;

import android.media.MediaExtractor;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Jni.FFmpegCmd;
import Jni.TrackUtils;
import Jni.VideoUitls;
import VideoHandle.CmdList;
import VideoHandle.EpDraw;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

/**
 * Created by Zipple on 2018/5/12.
 */

public class FFmpegEditor  {
    private static final int DEFAULT_WIDTH = 480;
    private static final int DEFAULT_HEIGHT = 360;

    private FFmpegEditor() {
    }

    public static void exec(EpVideo epVideo, FFmpegEditor.OutputOption outputOption, OnEditorListener onEditorListener) {
        boolean isFilter = false;
        ArrayList<EpDraw> epDraws = epVideo.getEpDraws();
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-y");
        if(epVideo.getVideoClip()) {
            cmd.append("-ss").append(epVideo.getClipStart()).append("-t").append(epVideo.getClipDuration()).append("-accurate_seek");
        }

        cmd.append("-i").append(epVideo.getVideoPath());
        StringBuilder filter_complex;
        if(epDraws.size() > 0) {
            for(int i = 0; i < epDraws.size(); ++i) {
                if(((EpDraw)epDraws.get(i)).isAnimation()) {
                    cmd.append("-ignore_loop");
                    cmd.append(0);
                }

                cmd.append("-i").append(((EpDraw)epDraws.get(i)).getPicPath());
            }

            cmd.append("-filter_complex");
            filter_complex = new StringBuilder();
            filter_complex.append("[0:v]").append(epVideo.getFilters() != null?epVideo.getFilters() + ",":"").append("scale=").append(outputOption.width == 0?"iw":Integer.valueOf(outputOption.width)).append(":").append(outputOption.height == 0?"ih":Integer.valueOf(outputOption.height)).append(outputOption.width == 0?"":",setdar=" + outputOption.getSar()).append("[outv0];");

            int i;
            for(i = 0; i < epDraws.size(); ++i) {
                filter_complex.append("[").append(i + 1).append(":0]").append(((EpDraw)epDraws.get(i)).getPicFilter()).append("scale=").append(((EpDraw)epDraws.get(i)).getPicWidth()).append(":").append(((EpDraw)epDraws.get(i)).getPicHeight()).append("[outv").append(i + 1).append("];");
            }

            for(i = 0; i < epDraws.size(); ++i) {
                if(i == 0) {
                    filter_complex.append("[outv").append(i).append("]").append("[outv").append(i + 1).append("]");
                } else {
                    filter_complex.append("[outo").append(i - 1).append("]").append("[outv").append(i + 1).append("]");
                }

                filter_complex.append("overlay=").append(((EpDraw)epDraws.get(i)).getPicX()).append(":").append(((EpDraw)epDraws.get(i)).getPicY()).append(((EpDraw)epDraws.get(i)).getTime());
                if(((EpDraw)epDraws.get(i)).isAnimation()) {
                    filter_complex.append(":shortest=1");
                }

                if(i < epDraws.size() - 1) {
                    filter_complex.append("[outo").append(i).append("];");
                }
            }

            cmd.append(filter_complex.toString());
            isFilter = true;
        } else {
            filter_complex = new StringBuilder();
            if(epVideo.getFilters() != null) {
                cmd.append("-filter_complex");
                filter_complex.append(epVideo.getFilters());
                isFilter = true;
            }

            if(outputOption.width != 0) {
                if(epVideo.getFilters() != null) {
                    filter_complex.append(",scale=").append(outputOption.width).append(":").append(outputOption.height).append(",setdar=").append(outputOption.getSar());
                } else {
                    cmd.append("-filter_complex");
                    filter_complex.append("scale=").append(outputOption.width).append(":").append(outputOption.height).append(",setdar=").append(outputOption.getSar());
                    isFilter = true;
                }
            }

            if(!filter_complex.toString().equals("")) {
                cmd.append(filter_complex.toString());
            }
        }

        cmd.append(outputOption.getOutputInfo().split(" "));
        if(!isFilter && outputOption.getOutputInfo().isEmpty()) {
            cmd.append("-vcodec");
            cmd.append("copy");
            cmd.append("-acodec");
            cmd.append("copy");
        } else {
            cmd.append("-preset");
            cmd.append("superfast");
        }

        cmd.append(outputOption.outPath);
        long duration = VideoUitls.getDuration(epVideo.getVideoPath());
        if(epVideo.getVideoClip()) {
            long clipTime = (long)((epVideo.getClipDuration() - epVideo.getClipStart()) * 1000000.0F);
            duration = clipTime < duration?clipTime:duration;
        }

        execCmd(cmd, duration, onEditorListener);
    }

    public static void merge(List<EpVideo> epVideos, FFmpegEditor.OutputOption outputOption, OnEditorListener onEditorListener) {
        Log.d("ffmpeg", "merge: 调用merge方法");
        boolean isNoAudioTrack = false;
        Iterator var4 = epVideos.iterator();

        int i;
        while(var4.hasNext()) {
            EpVideo epVideo = (EpVideo)var4.next();
            MediaExtractor mediaExtractor = new MediaExtractor();

            try {
                mediaExtractor.setDataSource(epVideo.getVideoPath());
            } catch (IOException var15) {
                var15.printStackTrace();
                return;
            }

            i = TrackUtils.selectAudioTrack(mediaExtractor);
            if(i == -1) {
                isNoAudioTrack = true;
                mediaExtractor.release();
                break;
            }

            mediaExtractor.release();
        }

        outputOption.width = outputOption.width == 0?1080:outputOption.width;
        outputOption.height = outputOption.height == 0?1920:outputOption.height;
        if(epVideos.size() <= 1) {
            throw new RuntimeException("Need more than one video");
        } else {
            CmdList cmd = new CmdList();
            cmd.append("ffmpeg");
            cmd.append("-y");

            Iterator var17;
            EpVideo e;
            for(var17 = epVideos.iterator(); var17.hasNext(); cmd.append("-i").append(e.getVideoPath())) {
                e = (EpVideo)var17.next();
                if(e.getVideoClip()) {
                    cmd.append("-ss").append(e.getClipStart()).append("-t").append(e.getClipDuration()).append("-accurate_seek");
                }
            }

            var17 = epVideos.iterator();

            while(true) {
                ArrayList epDraws;
                do {
                    if(!var17.hasNext()) {
                        cmd.append("-filter_complex");
                        StringBuilder filter_complex = new StringBuilder();

                        int drawNum;
                        for(drawNum = 0; drawNum < epVideos.size(); ++drawNum) {
                            StringBuilder filter = ((EpVideo)epVideos.get(drawNum)).getFilters() == null?new StringBuilder(""):((EpVideo)epVideos.get(drawNum)).getFilters().append(",");
                            filter_complex.append("[").append(drawNum).append(":v]").append(filter).append("scale=").append(outputOption.width).append(":").append(outputOption.height).append(",setdar=").append(outputOption.getSar()).append("[outv").append(drawNum).append("];");
                        }

                        drawNum = epVideos.size();

                        int j;
                        for(i = 0; i < epVideos.size(); ++i) {
                            for(j = 0; j < ((EpVideo)epVideos.get(i)).getEpDraws().size(); ++j) {
                                filter_complex.append("[").append(drawNum++).append(":0]").append(((EpDraw)((EpVideo)epVideos.get(i)).getEpDraws().get(j)).getPicFilter()).append("scale=").append(((EpDraw)((EpVideo)epVideos.get(i)).getEpDraws().get(j)).getPicWidth()).append(":").append(((EpDraw)((EpVideo)epVideos.get(i)).getEpDraws().get(j)).getPicHeight()).append("[p").append(i).append("a").append(j).append("];");
                            }
                        }

                        for(i = 0; i < epVideos.size(); ++i) {
                            for(j = 0; j < ((EpVideo)epVideos.get(i)).getEpDraws().size(); ++j) {
                                filter_complex.append("[outv").append(i).append("][p").append(i).append("a").append(j).append("]overlay=").append(((EpDraw)((EpVideo)epVideos.get(i)).getEpDraws().get(j)).getPicX()).append(":").append(((EpDraw)((EpVideo)epVideos.get(i)).getEpDraws().get(j)).getPicY()).append(((EpDraw)((EpVideo)epVideos.get(i)).getEpDraws().get(j)).getTime());
                                if(((EpDraw)((EpVideo)epVideos.get(i)).getEpDraws().get(j)).isAnimation()) {
                                    filter_complex.append(":shortest=1");
                                }

                                filter_complex.append("[outv").append(i).append("];");
                            }
                        }

                        for(i = 0; i < epVideos.size(); ++i) {
                            filter_complex.append("[outv").append(i).append("]");
                        }

                        filter_complex.append("concat=n=").append(epVideos.size()).append(":v=1:a=0[outv]");
                        if(!isNoAudioTrack) {
                            filter_complex.append(";");

                            for(i = 0; i < epVideos.size(); ++i) {
                                filter_complex.append("[").append(i).append(":a]");
                            }

                            filter_complex.append("concat=n=").append(epVideos.size()).append(":v=0:a=1[outa]");
                        }

                        if(!filter_complex.toString().equals("")) {
                            cmd.append(filter_complex.toString());
                        }

                        cmd.append("-map").append("[outv]");
                        if(!isNoAudioTrack) {
                            cmd.append("-map").append("[outa]");
                        }

                        cmd.append(outputOption.getOutputInfo().split(" "));
                        cmd.append("-preset").append("superfast").append(outputOption.outPath);
                        long duration = 0L;

                        long d;
                        for(Iterator var24 = epVideos.iterator(); var24.hasNext(); duration += d) {
                            EpVideo ep = (EpVideo)var24.next();
                            d = VideoUitls.getDuration(ep.getVideoPath());
                            if(ep.getVideoClip()) {
                                long clipTime = (long)((ep.getClipDuration() - ep.getClipStart()) * 1000000.0F);
                                d = clipTime < d?clipTime:d;
                            }

                            if(d == 0L) {
                                break;
                            }
                        }

                        execCmd(cmd, duration, onEditorListener);
                        return;
                    }

                    e = (EpVideo)var17.next();
                    epDraws = e.getEpDraws();
                } while(epDraws.size() <= 0);

                EpDraw ep;
                for(Iterator var8 = epDraws.iterator(); var8.hasNext(); cmd.append("-i").append(ep.getPicPath())) {
                    ep = (EpDraw)var8.next();
                    if(ep.isAnimation()) {
                        cmd.append("-ignore_loop").append(0);
                    }
                }
            }
        }
    }

    public static void mergeByLc(String root, List<EpVideo> epVideos, FFmpegEditor.OutputOption outputOption, OnEditorListener onEditorListener) {
        String fileName = "video_concat.txt";
        List<String> videos = new ArrayList<>();
        for (EpVideo e : epVideos) {
            videos.add(e.getVideoPath());
        }

        if (FileUtil.writeContentsToTxt(root+fileName,videos)){
            CmdList cmd = new CmdList();
            cmd.append("ffmpeg").append("-y").append("-f").append("concat").append("-safe").append("0").append("-i").append(root + fileName).append("-c").append("copy").append(outputOption.outPath);
            long duration = 0L;

            long d;
            for(Iterator var10 = epVideos.iterator(); var10.hasNext(); duration += d) {
                EpVideo ep = (EpVideo)var10.next();
                d = VideoUitls.getDuration(ep.getVideoPath());
                Log.d("ffmpeg", "mergeByLc: duration = "+d);
                if(d == 0L) {
                    break;
                }
            }
            Log.d("RecorderActivity", "mergeByLc: 开始执行合并视频命令");
            Log.d("ffmepg", "mergeByLc: 合并视频时长："+duration);
            execCmd(cmd, duration, onEditorListener);
        }else{
            Log.e("RecorderActivity", "mergeByLc: error in create concat file" );
        }

    }

    public static void music(String videoin, String audioin, String output, float videoVolume, float audioVolume, OnEditorListener onEditorListener) {
        MediaExtractor mediaExtractor = new MediaExtractor();

        try {
            mediaExtractor.setDataSource(videoin);
        } catch (IOException var11) {
            var11.printStackTrace();
            return;
        }

        int at = TrackUtils.selectAudioTrack(mediaExtractor);
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg").append("-y").append("-i").append(videoin);
        if(at == -1) {
            int vt = TrackUtils.selectVideoTrack(mediaExtractor);
            float duration = (float)mediaExtractor.getTrackFormat(vt).getLong("durationUs") / 1000.0F / 1000.0F;
            cmd.append("-ss").append("0").append("-t").append(duration).append("-i").append(audioin).append("-acodec").append("copy").append("-vcodec").append("copy");
        } else {
            cmd.append("-i").append(audioin).append("-filter_complex").append("[0:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=" + videoVolume + "[a0];[1:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=" + audioVolume + "[a1];[a0][a1]amix=inputs=2:duration=first[aout]").append("-map").append("[aout]").append("-ac").append("2").append("-c:v").append("copy").append("-map").append("0:v:0");
//            ffmpeg -y -i input.mp4 -i ainiyiwannian.wav -filter_complex "[0:a] pan=stereo|c0=1*c0|c1=1*c1 [a1], [1:a] pan=stereo|c0=1*c0|c1=1*c1 [a2],[a1][a2]amix=duration=first,pan=stereo|c0<c0+c1|c1<c2+c3,pan=mono|c0=c0+c1[a]" -map "[a]" -map 0:v -c:v libx264 -c:a aac -strict -2 -ac 2 output.mp4
//            cmd.append("-i").append(audioin).append("-filter_complex").append("[0:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=" + videoVolume + "[a0];[1:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=" + audioVolume).append("-ac").append("2").append("-c:v").append("copy").append("-map").append("0:v:0");
        }

        cmd.append(output);
        mediaExtractor.release();
        long d = VideoUitls.getDuration(videoin);
        execCmd(cmd, d, onEditorListener);
    }

    public static void reverse(String videoin, String out, boolean vr, boolean ar, OnEditorListener onEditorListener) {
        if(!vr && !ar) {
            Log.e("ffmpeg", "parameter error");
            onEditorListener.onFailure();
        } else {
            CmdList cmd = new CmdList();
            cmd.append("ffmpeg").append("-y").append("-i").append(videoin).append("-filter_complex");
            String filter = "";
            if(vr) {
                filter = filter + "[0:v]reverse[v];";
            }

            if(ar) {
                filter = filter + "[0:a]areverse[a];";
            }

            cmd.append(filter.substring(0, filter.length() - 1));
            if(vr) {
                cmd.append("-map").append("[v]");
            }

            if(ar) {
                cmd.append("-map").append("[a]");
            }

            if(ar && !vr) {
                cmd.append("-acodec").append("libmp3lame");
            }

            cmd.append("-preset").append("superfast").append(out);
            long d = VideoUitls.getDuration(videoin);
            execCmd(cmd, d, onEditorListener);
        }
    }


    public static void video2pic(String videoin, String out, int w, int h, float rate, OnEditorListener onEditorListener) {
        if(w > 0 && h > 0) {
            if(rate <= 0.0F) {
                Log.e("ffmpeg", "rate must greater than 0");
                onEditorListener.onFailure();
            } else {
                CmdList cmd = new CmdList();
                cmd.append("ffmpeg").append("-y").append("-i").append(videoin).append("-r").append(rate).append("-s").append(w + "x" + h).append("-q:v").append(2).append("-f").append("image2").append("-preset").append("superfast").append(out);
                long d = VideoUitls.getDuration(videoin);
                execCmd(cmd, d, onEditorListener);
            }
        } else {
            Log.e("ffmpeg", "width and height must greater than 0");
            onEditorListener.onFailure();
        }
    }

    public static void pic2video(String videoin, String out, int w, int h, float rate, OnEditorListener onEditorListener) {
        if(w >= 0 && h >= 0) {
            if(rate <= 0.0F) {
                Log.e("ffmpeg", "rate must greater than 0");
                onEditorListener.onFailure();
            } else {
                CmdList cmd = new CmdList();
                cmd.append("ffmpeg").append("-y").append("-f").append("image2").append("-i").append(videoin).append("-vcodec").append("libx264").append("-r").append(rate);
                if(w > 0 && h > 0) {
                    cmd.append("-s").append(w + "x" + h);
                }

                cmd.append(out);
                long d = VideoUitls.getDuration(videoin);
                execCmd(cmd, d, onEditorListener);
            }
        } else {
            Log.e("ffmpeg", "width and height must greater than 0");
            onEditorListener.onFailure();
        }
    }

    public static void execCmd(String cmd, long duration, final OnEditorListener onEditorListener) {
        cmd = "ffmpeg " + cmd;
        String[] cmds = cmd.split(" ");
        FFmpegCmd.exec(cmds, duration, new OnEditorListener() {
            public void onSuccess() {
                onEditorListener.onSuccess();
            }

            public void onFailure() {
                onEditorListener.onFailure();
            }

            public void onProgress(float progress) {
                onEditorListener.onProgress(progress);
            }
        });
    }

    private static void execCmd(CmdList cmd, long duration, OnEditorListener onEditorListener) {
        String[] cmds = (String[])cmd.toArray(new String[cmd.size()]);
        String cmdLog = "";
        String[] var6 = cmds;
        int var7 = cmds.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String var10000 = var6[var8];
            cmdLog = cmdLog + cmds;
        }

        Log.v("EpMediaF", "cmd:" + cmdLog);
        FFmpegCmd.exec(cmds, duration, onEditorListener);
    }

    public static class OutputOption {
        static final int ONE_TO_ONE = 1;
        static final int FOUR_TO_THREE = 2;
        static final int SIXTEEN_TO_NINE = 3;
        static final int NINE_TO_SIXTEEN = 4;
        static final int THREE_TO_FOUR = 5;
        String outPath;
        public int frameRate = 0;
        public int bitRate = 0;
        public String outFormat = "";
        private int width = 0;
        private int height = 0;
        private int sar = 6;

        public OutputOption(String outPath) {
            this.outPath = outPath;
        }

        public String getSar() {
            String res;
            switch(this.sar) {
                case 1:
                    res = "1/1";
                    break;
                case 2:
                    res = "4/3";
                    break;
                case 3:
                    res = "16/9";
                    break;
                case 4:
                    res = "9/16";
                    break;
                case 5:
                    res = "3/4";
                    break;
                default:
                    res = this.width + "/" + this.height;
            }

            return res;
        }

        public void setSar(int sar) {
            this.sar = sar;
        }

        String getOutputInfo() {
            StringBuilder res = new StringBuilder();
            if(this.frameRate != 0) {
                res.append(" -r ").append(this.frameRate);
            }

            if(this.bitRate != 0) {
                res.append(" -b ").append(this.bitRate).append("M");
            }

            if(!this.outFormat.isEmpty()) {
                res.append(" -f ").append(this.outFormat);
            }

            return res.toString();
        }

        public void setWidth(int width) {
            if(width % 2 != 0) {
                --width;
            }

            this.width = width;
        }

        public void setHeight(int height) {
            if(height % 2 != 0) {
                --height;
            }

            this.height = height;
        }
    }

    public static enum PTS {
        VIDEO,
        AUDIO,
        ALL;

        private PTS() {
        }
    }

    public static enum Format {
        MP3,
        MP4;

        private Format() {
        }
    }
}
