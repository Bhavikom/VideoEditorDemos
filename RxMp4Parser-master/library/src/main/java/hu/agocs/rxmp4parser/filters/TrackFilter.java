package hu.agocs.rxmp4parser.filters;

import org.mp4parser.muxer.Track;

import rx.functions.Func1;

public class TrackFilter implements Func1<Track, Boolean> {

    private final String handlerName;

    public TrackFilter(String handlerName) {
        this.handlerName = handlerName;
    }

    @Override
    public Boolean call(Track track) {
        return handlerName != null && handlerName.equals(track.getHandler());
    }
}