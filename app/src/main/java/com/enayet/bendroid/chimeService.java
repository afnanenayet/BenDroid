package com.enayet.bendroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class chimeService extends Service {
    public chimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
