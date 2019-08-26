package hu.agocs.rxmp4parser.operators;

import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;

import java.util.ArrayList;
import java.util.List;

import hu.agocs.rxmp4parser.Utils;
import rx.Observable;
import rx.Subscriber;

public class MuxTracks implements Observable.Operator<Movie, Iterable<? extends Track>> {

    @Override
    public Subscriber<? super Iterable<? extends Track>> call(final Subscriber<? super Movie> subscriber) {

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
                    subscriber.onNext(Utils.mux(tracks));
                }
            }
        };
    }
}
