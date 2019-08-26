package hu.agocs.rxmp4parser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.mp4parser.Container;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.TrackMetaData;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;
import org.mp4parser.muxer.tracks.AppendTrack;
import org.mp4parser.muxer.tracks.h264.parsing.model.BitstreamElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import hu.agocs.rxmp4parser.filters.AudioTrackFilter;
import hu.agocs.rxmp4parser.filters.NullFilter;
import hu.agocs.rxmp4parser.filters.TrackFilter;
import hu.agocs.rxmp4parser.filters.VideoTrackFilter;
import hu.agocs.rxmp4parser.operators.AppendTracks;
import hu.agocs.rxmp4parser.operators.CropMovie;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;

public class RxMp4Parser {

    private static final String TAG = "RxMp4Parser";

    public static Observable<Movie> from(@NonNull final String inputPath) {
        return Observable.defer(new Func0<Observable<Movie>>() {
            @Override
            public Observable<Movie> call() {
                if (new File(inputPath).exists()) {
                    try {
                        return Observable.just(MovieCreator.build(inputPath));
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                        return Observable.error(e);
                    }
                } else {
                    return Observable.error(new FileNotFoundException(inputPath));
                }
            }
        });
    }

    public static Observable<Movie> from(@NonNull final File inputFile) {
        return from(inputFile.getAbsolutePath());
    }

    @Nullable
    public static Observable<Track> extractTrackWithHandler(@NonNull final Movie movie, @NonNull final String handler) {
        return Observable.from(movie.getTracks())
                .filter(new TrackFilter(handler))
                .firstOrDefault(null);
    }

    @Nullable
    public static Observable<Track> extractAudioTrack(@NonNull final Movie movie) {
        return Observable.from(movie.getTracks())
                .filter(new AudioTrackFilter())
                .firstOrDefault(null);
    }

    @Nullable
    public static Observable<Track> extractVideoTrack(final Movie movie) {
        return Observable.from(movie.getTracks())
                .filter(new VideoTrackFilter())
                .firstOrDefault(null);
    }

    @SafeVarargs
    public static Observable<Movie> concatenate(@NonNull Observable<Movie>... input) {
        return Observable.from(input)
                .toList()
                .flatMap(new Func1<List<Observable<Movie>>, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> call(List<Observable<Movie>> observables) {
                        return concatenate(observables);
                    }
                });
    }

    public static Observable<Movie> concatenate(@NonNull Iterable<? extends Observable<Movie>> input) {
        return Observable.zip(input, new FuncN<Iterable<Movie>>() {
            @Override
            public Iterable<Movie> call(Object... args) {
                List<Movie> movies = new ArrayList<>();
                for (Object movie : args) {
                    movies.add((Movie) movie);
                }
                return movies;
            }
        }).flatMap(new Func1<Iterable<Movie>, Observable<Movie>>() {
            @Override
            public Observable<Movie> call(Iterable<Movie> movies) {
                return Observable.zip(
                        Observable.from(movies)
                                .flatMap(new Func1<Movie, Observable<Track>>() {
                                    @Override
                                    public Observable<Track> call(Movie movie) {
                                        return extractAudioTrack(movie);
                                    }
                                })
                                .toList()
                                .lift(new AppendTracks()),
                        Observable.from(movies)
                                .flatMap(new Func1<Movie, Observable<Track>>() {
                                    @Override
                                    public Observable<Track> call(Movie movie) {
                                        return extractVideoTrack(movie);
                                    }
                                })
                                .toList()
                                .lift(new AppendTracks()),
                        new Func2<AppendTrack, AppendTrack, Movie>() {
                            @Override
                            public Movie call(AppendTrack audioTrack, AppendTrack videoTrack) {
                                return Utils.mux(audioTrack, videoTrack);
                            }
                        }
                );
            }
        });
    }

    @SafeVarargs
    public static Observable<File> concatenateInto(@NonNull final File outputFile, @NonNull Observable<Movie>... input) {
        return concatenate(input).flatMap(new Func1<Movie, Observable<File>>() {
            @Override
            public Observable<File> call(Movie movie) {
                return output(movie, outputFile);
            }
        });
    }

    public static Observable<File> concatenateInto(@NonNull Iterable<? extends Observable<Movie>> input, @NonNull final File outputFile) {
        return concatenate(input).flatMap(new Func1<Movie, Observable<File>>() {
            @Override
            public Observable<File> call(Movie movie) {
                return output(movie, outputFile);
            }
        });
    }

    public static Observable<Movie> crop(@NonNull String filePath, double fromTime, double toTime) {
        return from(filePath).lift(new CropMovie(fromTime, toTime));
    }

    public static Observable<Movie> crop(@NonNull final File inputFile, final double fromTime, final double toTime) {
        return crop(inputFile.getAbsolutePath(), fromTime, toTime);
    }

    public static Observable<File> output(final Movie movie, final File outputFile) {
        return Observable.defer(new Func0<Observable<File>>() {
            @Override
            public Observable<File> call() {
                try {
                    Container output = new DefaultMp4Builder().build(movie);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    FileChannel fc = fos.getChannel();
                    fc.position(0);
                    output.writeContainer(fc);
                    fc.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Observable.error(e);
                }
                return Observable.just(outputFile);
            }
        });
    }

    public static Observable<File> output(Movie movie, String outputPath) {
        return output(movie, new File(outputPath));
    }

    public static Observable<TrackMetaData> getMetaDataByTrackId(final Movie movie, final long trackId) {
        return Observable.defer(new Func0<Observable<TrackMetaData>>() {
            @Override
            public Observable<TrackMetaData> call() {
                if (movie == null) {
                    return Observable.error(new Throwable("Movie is null"));
                }
                Track selectedTrack = movie.getTrackByTrackId(trackId);
                if (selectedTrack == null) {
                    return Observable.error(new Throwable("The provided input duesn't contain track with ID: " + trackId));
                }
                return Observable.just(selectedTrack.getTrackMetaData());
            }
        });
    }

    public static Observable<List<TrackMetaData>> getAllMetaData(Movie movie) {
        return Observable.from(movie.getTracks())
                .filter(new NullFilter())
                .map(new Func1<Track, TrackMetaData>() {
                    @Override
                    public TrackMetaData call(Track track) {
                        return track.getTrackMetaData();
                    }
                })
                .filter(new NullFilter())
                .toList();
    }

}
