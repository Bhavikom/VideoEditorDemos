# RxMp4Parser

More and more Android apps provide video editing based features. This library is not a complete wrapper for the most recent Mp4Parser library, but it provides a few useful features which are usually used by these apps. If it does fit your application's needs, feel free to implement those features based on the current implementation. The implemented features' restrictions are the same as the base library's restrictions. See: https://github.com/sannies/mp4parser

## Usage

The base class of the library is RxMp4Parser. This provides the basic functionality for:

  - Loading files into Movie objects
  - Concatenation
  - Crop
  - Extract track specified by their handlers (Audio/Video/etc.)
  - Muxing tracks into Movie
  - Save Movie to output file

## Functions
| **Function** | **Return type** | **Description** |
| --- | --- | --- |
| `RxMp4Parser.from(File input)`<br/>`RxMp4Parser.from(String inputFilePath)` | `Observable<Movie>`	| Reads the specified file and creates a Movie instance from it. The result types are Observable<Movie> for both functions. |
| `RxMp4Parser.extractVideoTrack(Movie movie)`<br/>`RxMp4Parser.extractAudioTrack(Movie movie)`<br/>`RxMp4Parser.extractTrackWithHandler(Movie movie, String handler)` | `Observable<Track>` |	As the method names show these methods extract a specific Track from the input Movie object, if it has one. If it has more than one the first occurrence will be returned. If the passed Movie doesn't contain a Track matching the given handler type, the Observable will return null. |
|`RxMp4Parser.concatenate(Observable<Movie>... input)`<br/>`RxMp4Parser.concatenate(Iterable<? extends Observable<Movie>> input)` | `Observable<Movie>` | Concatenates the given Observable<Movie> instances according to the parameter order. The returned Movie instance contains the concatenated output. |
|`RxMp4Parser.concatenateInto(File outputFile, Observable<Movie>... input)`<br/>`RxMp4Parser.concatenateInto(File outputFile, Iterable<? extends Observable<Movie>> input)`|	`Observable<File>`|	Does the same as the previous methods, but you can specify the output file for the result. After the concatenation it will write out the Movie object and return the File reference pointing on it. (Which you specified in the parameters.) |
|`RxMp4Parser.crop(String filePath, double fromTime, double toTime)`<br/>`RxMp4Parser.crop(File inputFile, double fromTime, double toTime)` | `Observable<Movie>` | This method returns a Movie instance which contains a cropped part of the original Movie. The cropped part is specified by the fromTime and toTime parameters in seconds! The accuracy of the cropping points is determined by the number and distribution of sync samples. |
|`RxMp4Parser.output(Movie movie, File outputFile)`<br/>`RxMp4Parser.output(Movie movie, String outputPath)` | `Observable<File>` | The given Movie object will be written out to the specified output file. The returned File reference points to the resulting file. |

## Operators

These classes can be used with the Observable.lift() function on the specified Observable<T> types. Most of the built in functions in the RxMp4Parser class also uses these operators.

### - AppendTracks:
This can be used on an Observable which wraps an Iterable<Track> instance and it produces an Observable which wraps a Track implementation (AppendTrack) which contains the received Tracks appended according to their original order.
    
```java
Observable<List<Track>> audioTracksObservable = ...;
Observable<Track> appendedTrackObservable = audioTracksObservable.lift(new AppendTracks());
  ```

### - CropMovie:
Calling this on an Observable<Movie> instance will produce a new Observable<Movie> object which contains only part of the original Movie specified by the Operators constructor:
    
  ```java
Observable<Movie> movieObservable = ...;
Observable<Movie> croppedMovieObservable = movieObservable.lift(new CropMovie(3.0f, 5.0f));
  ```
      
The first parameter is the starting time and the second one is the ending time in seconds. The output will contain the Movie part between the two time marker.

### - MuxTracks:
This operator is use to mux the supplied Tracks into a Movie instance. It can be used on an Observable<Iterable<Track>> instance and will return an Observable<Movie> object.
    
  ```java
Observable<List<Track>> tracksObservable = ...;
Observable<Movie> muxedMovieObservable = tracksObservable.lift(new MuxTracks());
  ```
  
## Example

Part of the sample MainActivity.java

  ```java 
File input = new File(getCacheDir() + "/sample.mp4");
File output = new File(Environment.getExternalStorageDirectory() + "/temp.mp4");
 
RxMp4Parser.concatenateInto(output,
        //The output, where should be stored the resulting Movie object
        output,
        //A full video
        RxMp4Parser.from(input),
        //Cropped video
        RxMp4Parser.crop(input, 8.5f, 13f),
        //Cropped video
        RxMp4Parser.crop(input.getAbsolutePath(), 5, 10),
        //Another full video
        RxMp4Parser.from(input.getAbsolutePath())
            .lift(new CropMovie(18f, 20f))
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<File>() {
            @Override
            public void call(File file) {
                //The process finished, you can use your modified video file!
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
               //Error occured during the process
            }
        });
  ```
  
## Final thoughts

This is not a final nor a complete implementation of a wrapper, but it is useful for video editing basics. Also, it helps to implement a project specific version when other features needed. The base library mainly uses synchronous calls for its tasks, and in my opinion, the wrapper helps a lot to eliminate the thread handling overhead from an Android App's base code.

## Future

The code used in this post is provided as is. If I'll have time and need other use-cases from the base library, I'll extend the functionality. Feel free to contribute to it...
