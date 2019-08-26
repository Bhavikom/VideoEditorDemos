// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.io.InputStream;

class ExifReader
{
    private final ExifInterface a;
    
    ExifReader(final ExifInterface a) {
        this.a = a;
    }
    
    protected ExifData read(final InputStream inputStream, final int n) {
        final ExifParser parse = ExifParser.parse(inputStream, n, this.a);
        final ExifData exifData = new ExifData(parse.getByteOrder());
        exifData.setSections(parse.getSections());
        exifData.mUncompressedDataPosition = parse.getUncompressedDataPosition();
        exifData.setQualityGuess(parse.getQualityGuess());
        exifData.setJpegProcess(parse.getJpegProcess());
        final int imageWidth = parse.getImageWidth();
        final int imageLength = parse.getImageLength();
        if (imageWidth > 0 && imageLength > 0) {
            exifData.setImageSize(imageWidth, imageLength);
        }
        for (int i = parse.next(); i != 5; i = parse.next()) {
            switch (i) {
                case 0: {
                    exifData.addIfdData(new IfdData(parse.getCurrentIfd()));
                    break;
                }
                case 1: {
                    final ExifTag tag = parse.getTag();
                    if (!tag.hasValue()) {
                        parse.registerForTagValue(tag);
                        break;
                    }
                    if (parse.isDefinedTag(tag.getIfd(), tag.getTagId())) {
                        exifData.getIfdData(tag.getIfd()).setTag(tag);
                        break;
                    }
                    TLog.w("%s skip tag because not registered in the tag table:%s", "ExifReader", tag);
                    break;
                }
                case 2: {
                    final ExifTag tag2 = parse.getTag();
                    if (tag2.getDataType() == 7) {
                        parse.readFullTagValue(tag2);
                    }
                    exifData.getIfdData(tag2.getIfd()).setTag(tag2);
                    break;
                }
                case 3: {
                    final byte[] compressedThumbnail = new byte[parse.getCompressedImageSize()];
                    if (compressedThumbnail.length == parse.read(compressedThumbnail)) {
                        exifData.setCompressedThumbnail(compressedThumbnail);
                        break;
                    }
                    TLog.w("%s Failed to read the compressed thumbnail", "ExifReader");
                    break;
                }
                case 4: {
                    final byte[] array = new byte[parse.getStripSize()];
                    if (array.length == parse.read(array)) {
                        exifData.setStripBytes(parse.getStripIndex(), array);
                        break;
                    }
                    TLog.w("%s Failed to read the strip bytes", "ExifReader");
                    break;
                }
            }
        }
        return exifData;
    }
}
