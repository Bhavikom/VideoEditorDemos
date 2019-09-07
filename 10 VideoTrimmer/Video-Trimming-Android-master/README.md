# Video Trimming - Android #
<a target="_blank" href="LICENSE"><img src="https://img.shields.io/badge/licence-MIT-brightgreen.svg" alt="license : MIT"></a>
<a target="_blank" href="https://www.cmarix.com/android-application-development-services.html"><img src="https://img.shields.io/badge/platform-android-blue.svg" alt="license : MIT"></a>

## Core Features ##

 - Video can be trimmed/shortened and played on the same screen
 - Video can be trimmed by selecting the starting point and ending point, and it will display the video size and video duration based on the selected position
 - Seekbar moves as per selected video for trimming
 - After trimming, trimmed video can be played automatically and can be shared on social media

## How it works ##

 - User can record video from camera or select video from gallery
 - Set the selected video in the trimming screen
 - Trim the video by dragging starting point and end point
 - View trimmed video on the trimming screen
 - Save video, it will automatically play in next screen
 - Share the video

## Purpose of this code ##

 - Whenever it is required to crop thr video, this code can help you
 - Whenever you are having a limiation of video recording such as allow users to record video for 1 min, this code can help you


## Requirements ##

 - Android 4.1+ 

## When you can use this code ##

 - When you are developing a Social or standalone video sharing app, this code will help you to provide functinality of trimming video and sharing video with user friendly operations.

## Code Snippet ##

**Step 1**: Send Video URL To Video Trimming class

    if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(60);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnK4LVideoListener(this);
            //mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
            mVideoTrimmer.setVideoURI(Uri.parse(path));
            mVideoTrimmer.setVideoInformationVisibility(true);
        }
**Step 2**: Initializtion of trimming views as we have declared in xml files

	LayoutInflater.from(context).inflate(R.layout.view_time_line, this, true);
	
	mHolderTopView = ((SeekBar) findViewById(R.id.handlerTop));
	mVideoProgressIndicator = ((ProgressBarView) findViewById(R.id.timeVideoView));
	mRangeSeekBarView = ((RangeSeekBarView) findViewById(R.id.timeLineBar));
	mLinearVideo = ((RelativeLayout) findViewById(R.id.layout_surface_view));
	mVideoView = ((VideoView) findViewById(R.id.video_loader));
	mPlayView = ((ImageView) findViewById(R.id.icon_video_play));
	mTimeInfoContainer = findViewById(R.id.timeText);
	mTextSize = ((TextView) findViewById(R.id.textSize));
	mTextTimeFrame = ((TextView) findViewById(R.id.textTimeSelection));
	mTextTime = ((TextView) findViewById(R.id.textTime));
	mTimeLineView = ((TimeLineView) findViewById(R.id.timeLineView));
	
	setUpListeners();
	setUpMargins();
        
 
**Step 3**: To prepare video as it is trimmed by user onVideoPrepared() method is used
  

    private void onVideoPrepared(@NonNull MediaPlayer mp) {
        // Adjust the size of the video
        // so it fits on the screen
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = mLinearVideo.getWidth();
        int screenHeight = mLinearVideo.getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        mVideoView.setLayoutParams(lp);

        mPlayView.setVisibility(View.VISIBLE);

        mDuration = mVideoView.getDuration();
        setSeekBarPosition();

        setTimeFrames();
        setTimeVideo(0);

        if (mOnK4LVideoListener != null) {
            mOnK4LVideoListener.onVideoPrepared();
        }
    }


**Step 4**: Give position for seekbar fom start to end

    private void setSeekBarPosition() {

        if (mDuration >= mMaxDuration) {
            mStartPosition = mDuration / 2 - mMaxDuration / 2;
            mEndPosition = mDuration / 2 + mMaxDuration / 2;

            mRangeSeekBarView.setThumbValue(0, (mStartPosition * 100) / mDuration);
            mRangeSeekBarView.setThumbValue(1, (mEndPosition * 100) / mDuration);

        } else {
            mStartPosition = 0;
            mEndPosition = mDuration;
        }

        setProgressBarPosition(mStartPosition);
        mVideoView.seekTo(mStartPosition);

        mTimeVideo = mDuration;
        mRangeSeekBarView.initMaxWidth();
    }


 **Step 5**: To give permission for seekbar to start moving from start to end, setSeekBarPosition() is used 
 

    private void setSeekBarPosition() {

        if (mDuration >= mMaxDuration) {
            mStartPosition = mDuration / 2 - mMaxDuration / 2;
            mEndPosition = mDuration / 2 + mMaxDuration / 2;

            mRangeSeekBarView.setThumbValue(0, (mStartPosition * 100) / mDuration);
            mRangeSeekBarView.setThumbValue(1, (mEndPosition * 100) / mDuration);

        } else {
            mStartPosition = 0;
            mEndPosition = mDuration;
        }
        setProgressBarPosition(mStartPosition);
        mVideoView.seekTo(mStartPosition);
        mTimeVideo = mDuration;
        mRangeSeekBarView.initMaxWidth();
    }
    

 **Step 6**: To synchronize video frames with time setTimeFrames() is used
 


    private void setTimeFrames() {
        String seconds = getContext().getString(R.string.short_seconds);
        mTextTimeFrame.setText(String.format("%s %s - %s %s", stringForTime(mStartPosition), seconds, stringForTime(mEndPosition), seconds));
    }


 **Step 7**: Set Start and end point of that video using onSeekThumbs() method

    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case Thumb.LEFT: {
                mStartPosition = (int) ((mDuration * value) / 100L);
                mVideoView.seekTo(mStartPosition);
                break;
            }
            case Thumb.RIGHT: {
                mEndPosition = (int) ((mDuration * value) / 100L);
                break;
            }
        }
        setProgressBarPosition(mStartPosition);

        setTimeFrames();
        mTimeVideo = mEndPosition - mStartPosition;
    }


## Let us know! ##
We’d be really happy if you sent us links to your projects where you use our component. Just send an email to [biz@cmarix.com](mailto:biz@cmarix.com "biz@cmarix.com") and do let us know if you have any questions or suggestion regarding video trimming in Android.

P.S. We’re going to publish more awesomeness examples on third party libraries, coding standards, plugins etc, in all the technology. Stay tuned!

## Stay Socially Connected ##

Get more familiar with our work by visiting few of our portfolio links.

[Portfolio](https://www.cmarix.com/portfolio.html) | [Facebook](https://www.facebook.com/CMARIXTechnoLabs/) | [Twitter](https://twitter.com/CMARIXTechLabs) | [Linkedin](https://www.linkedin.com/company/cmarix-technolabs-pvt-ltd-) | [Behance](https://www.behance.net/CMARIXTechnoLabs/) | [Instagram](https://instagram.com/cmarixtechnolabs/) | [Dribbble](https://dribbble.com/CMARIXTechnoLabs) | [Uplabs](https://www.uplabs.com/cmarixtechnolabs)

Please don’t forget to follow them.

## License ##

	MIT License
	
	Copyright © 2019 CMARIX TechnoLabs
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
