package hu.agocs.rxmp4parser;

import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;

import java.util.Arrays;

public class Utils {

    public static Movie mux(Iterable<? extends Track> tracks) {
        Movie movie = new Movie();
        for (Track track : tracks) {
            movie.addTrack(track);
        }
        return movie;
    }

    public static Movie mux(Track... tracks) {
        Movie movie = new Movie();
        for (Track track : tracks) {
            movie.addTrack(track);
        }
        return movie;
    }

    public static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];

            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                // samples always start with 1 but we start with zero therefore +1
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;

        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }

}
