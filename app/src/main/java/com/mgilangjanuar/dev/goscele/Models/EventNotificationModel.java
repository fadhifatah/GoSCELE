package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

/**
 * Created by muhammadgilangjanuar on 5/29/17.
 */

public class EventNotificationModel extends BaseRecord {

    public String date;
    public boolean isEnableAlarm;

    public EventNotificationModel(Context context) {
        super(context);
    }

    @Override
    public String tag() {
        return "EventNotificationModel";
    }

    @Override
    public void save() {
        prefsEditor.putBoolean(getAttributeTag("isEnableAlarm"), this.isEnableAlarm);
        prefsEditor.putString(getAttributeTag("date"), this.date);
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putBoolean(getAttributeTag("isEnableAlarm"), false);
        prefsEditor.putString(getAttributeTag("date"), null);
        prefsEditor.commit();
    }

    public String getSavedDate() {
        date = sharedPreferences.getString(getAttributeTag("date"), null);
        return date;
    }

    public boolean getSavedIsEnableAlarm() {
        isEnableAlarm = sharedPreferences.getBoolean(getAttributeTag("isEnableAlarm"), false);
        return isEnableAlarm;
    }
}
