package hu.agocs.rxmp4parser.operators;

import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.tracks.AppendTrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class AppendTracks implements Observable.Operator<AppendTrack, Iterable<? extends Track>> {

    @Override
    public Subscriber<? super Iterable<? extends Track>> call(final Subscriber<? super AppendTrack> subscriber) {

        return new Subscriber<Iterable<? extends Track>>() {
            @Override
            public void onCompleted() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }

            @Override
            public void onNext(Iterable<? extends Track> tracks) {
                if (!subscriber.isUnsubscribed()) {
                    List<Track> trackList = new ArrayList<>();
                    for (Track track : tracks) {
                        trackList.add(track);
                    }
                    try {
                        subscriber.onNext(new AppendTrack(trackList.toArray(new Track[trackList.size()])));
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        };
    }
}
