package ru.c17.balance.animation;

import android.os.Parcel;
import android.os.Parcelable;


public class RevealAnimationSetting implements Parcelable {

    private final int describeContents;

    private final int centerX;

    private final int centerY;

    private final int width;

    private final int height;

    public RevealAnimationSetting(
            int centerX,
            int centerY,
            int width,
            int height) {
        this.describeContents = 0;
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    private RevealAnimationSetting(Parcel in) {
        describeContents = in.readInt();
        centerX = in.readInt();
        centerY = in.readInt();
        width = in.readInt();
        height = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(describeContents);
        dest.writeInt(centerX);
        dest.writeInt(centerY);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    public static final Creator<RevealAnimationSetting> CREATOR = new Creator<RevealAnimationSetting>() {
        @Override
        public RevealAnimationSetting createFromParcel(Parcel in) {
            return new RevealAnimationSetting(in);
        }

        @Override
        public RevealAnimationSetting[] newArray(int size) {
            return new RevealAnimationSetting[size];
        }
    };

    public int describeContents() {
        return describeContents;
    }

    int getCenterX() {
        return centerX;
    }

    int getCenterY() {
        return centerY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof RevealAnimationSetting) {
            RevealAnimationSetting that = (RevealAnimationSetting) o;
            return this.describeContents == that.describeContents()
                    && this.centerX == that.getCenterX()
                    && this.centerY == that.getCenterY()
                    && this.width == that.getWidth()
                    && this.height == that.getHeight();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h$ = 1;
        h$ *= 1000003;
        h$ ^= describeContents;
        h$ *= 1000003;
        h$ ^= centerX;
        h$ *= 1000003;
        h$ ^= centerY;
        h$ *= 1000003;
        h$ ^= width;
        h$ *= 1000003;
        h$ ^= height;
        return h$;
    }
}
