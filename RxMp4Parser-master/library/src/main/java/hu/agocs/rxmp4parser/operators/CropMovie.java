package hu.agocs.rxmp4parser.operators;

import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.tracks.AppendTrack;
import org.mp4parser.muxer.tracks.ClippedTrack;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import hu.agocs.rxmp4parser.Utils;
import rx.Observable;
import rx.Subscriber;

public class CropMovie implements Observable.Operator<Movie, Movie> {

    private final double from;
    private final double to;

    public CropMovie(double from, double to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Subscriber<? super Movie> call(final Subscriber<? super Movie> subscriber) {
        return new Subscriber<Movie>() {
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
            public void onNext(Movie movie) {
                if (to < from) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(new RuntimeException("The ending time is earlier than the start time."));
                    }
                }

                List<Track> tracks = movie.getTracks();
                movie.setTracks(new LinkedList<Track>());
                // remove all tracks we will create new tracks from the old

                double startTime = from;
                double endTime = to;

                boolean timeCorrected = false;

                // Here we try to find a track that has sync samples. Since we can only start decoding
                // at such a sample we SHOULD make sure that the start of the new fragment is exactly
                // such a frame
                for (Track track : tracks) {
                    if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                        if (timeCorrected) {
                            // This exception here could be a false positive in case we have multiple tracks
                            // with sync samples at exactly the same positions. E.g. a single movie containing
                            // multiple qualities of the same video (Microsoft Smooth Streaming file)
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onError(new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported."));
                            }
                        }
                        startTime = Utils.correctTimeToSyncSample(track, startTime, false);
                        endTime = Utils.correctTimeToSyncSample(track, endTime, true);
                        timeCorrected = true;
                    }
                }
                try {
                    for (Track track : tracks) {
                        long currentSample = 0;
                        double currentTime = 0;
                        double lastTime = -1;
                        long startSample = -1;
                        long endSample = -1;

                        for (int i = 0; i < track.getSampleDurations().length; i++) {
                            long delta = track.getSampleDurations()[i];

                            if (currentTime > lastTime && currentTime <= startTime) {
                                // current sample is still before the new starttime
                                startSample = currentSample;
                            }
                            if (currentTime > lastTime && currentTime <= endTime) {
                                // current sample is after the new start time and still before the new endtime
                                endSample = currentSample;
                            }
                            lastTime = currentTime;
                            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
                            currentSample++;
                        }

                        movie.addTrack(new AppendTrack(new ClippedTrack(track, startSample, endSample)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(movie);
                }
            }
        };
    }
}
