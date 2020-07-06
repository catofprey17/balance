package ru.c17.balance.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ru.c17.balance.App;
import ru.c17.balance.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "receipts",
        indices = {@Index(value = {"fn", "fp", "fp"}, unique = true), @Index(value = "period_id")},
        foreignKeys = @ForeignKey(entity = Period.class, parentColumns = "id", childColumns = "period_id", onDelete = ForeignKey.CASCADE))
public class Receipt implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "fn")
    private String fn;

    @ColumnInfo(name = "fp")
    private String fp;

    @ColumnInfo(name = "fd")
    private String fd;

    @ColumnInfo(name = "sum")
    private Float sum;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "period_id")
    private long periodId;

    @ColumnInfo(name = "n")
    private String n;

    @ColumnInfo(name = "is_downloaded")
    private boolean isDownloaded;

    @ColumnInfo(name = "store_name")
    private String storeName;

    @ColumnInfo(name = "inn")
    private String inn;

    @ColumnInfo(name = "categoryId")
    private int categoryId;

    @ColumnInfo(name = "is_added_manually")
    private boolean isAddedManually;

    @ColumnInfo(name = "qr_string")
    private String qrString;

    @ColumnInfo
    private String address;



    public Receipt() {
        isDownloaded = false;
        storeName = "";
    }


    protected Receipt(Parcel in) {
        id = in.readLong();
        fn = in.readString();
        fp = in.readString();
        fd = in.readString();
        if (in.readByte() == 0) {
            sum = null;
        } else {
            sum = in.readFloat();
        }
        time = in.readLong();
        periodId = in.readLong();
        n = in.readString();
        isDownloaded = in.readByte() != 0;
        storeName = in.readString();
        inn = in.readString();
        categoryId = in.readInt();
        isAddedManually = in.readByte() != 0;
        qrString = in.readString();
        address = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }


    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private void setTime(String str) {
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(str.substring(0, 4));
        int month = Integer.parseInt(str.substring(4,6)) - 1;
        int dayOfMonth = Integer.parseInt(str.substring(6,8));
        int hours = Integer.parseInt(str.substring(9,11));
        int minutes = Integer.parseInt(str.substring(11, 13));
        calendar.set(year, month, dayOfMonth, hours, minutes);
        this.time = calendar.getTimeInMillis();
    }

    public long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(long periodId) {
        this.periodId = periodId;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isAddedManually() {
        return isAddedManually;
    }

    public void setAddedManually(boolean addedManually) {
        isAddedManually = addedManually;
    }

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Receipt parseQR(String qrString) {
        if (!qrString.contains("&")) {
            throw new IllegalArgumentException();
        }
        String[] args = qrString.split("&");
        if (args.length != 6) {
            throw new IllegalArgumentException();
        }
        Receipt receipt = new Receipt();
        receipt.setTime(args[0].split("=")[1]);
        receipt.setSum(Float.valueOf(args[1].split("=")[1]));
        receipt.setFn(args[2].split("=")[1]);
        receipt.setFd(args[3].split("=")[1]);
        receipt.setFp(args[4].split("=")[1]);
        receipt.setN(args[5].split("=")[1]);
        receipt.setQrString(qrString);
        return receipt;

    }

    public String getSumForDisplay() {
        return String.format(Locale.getDefault(), "%.2f", sum) + " ₽";
    }

    public String getSumForRequest() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        return String.format(Locale.getDefault(),"%.2f", sum).replace(String.valueOf(symbols.getDecimalSeparator()), "");
    }

    public String getConvertedTimeForRequest() {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    public String getConvertedTimeForDisplay() {
        //        Date date = new Date(time);
        //        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        //        return sdf.format(date);
        return DateUtils.formatDateTime(App.getContext(), time,
                DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR);
    }

    public Period getPeriod() {
        Period period = new Period();
        period.setMonthAndYearInMills(time);
        return period;
    }

    public String getStoreNameForDisplay(Context context) {
        if (storeName.equals("")) {
            return context.getResources().getString(R.string.store);
        } else {
            String result = getStoreName();
            result = result.replaceAll("ООО ", "");
            result = result.replaceAll("АКЦИОНЕРНОЕ ОБЩЕСТВО ", "");
            result = result.replaceAll("ЗАО ", "");
            result = result.replaceAll("\"", "");
            return result;
        }
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(fn);
        dest.writeString(fp);
        dest.writeString(fd);
        if (sum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(sum);
        }
        dest.writeLong(time);
        dest.writeLong(periodId);
        dest.writeString(n);
        dest.writeByte((byte) (isDownloaded ? 1 : 0));
        dest.writeString(storeName);
        dest.writeString(inn);
        dest.writeInt(categoryId);
        dest.writeByte((byte) (isAddedManually ? 1 : 0));
        dest.writeString(qrString);
    }
}
