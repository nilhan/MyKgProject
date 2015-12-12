package com.example.nilhan.anaokulu.util;

import android.telephony.SmsManager;

import com.example.nilhan.anaokulu.model.Beacon;
import com.example.nilhan.anaokulu.model.StudentList;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by nilhan on 06.12.2015.
 */
public class CommunicationManager {
    private Hashtable<Beacon,Long> inVehicleList = new Hashtable<Beacon,Long>();
    private StudentList studentList;

    private long nonExistenceDuration = 60000;
    private InVehicleControlThread inVehicleControlThread;
    public static int counter = 0;


    public CommunicationManager(){
    }

    public void notifyBeaconDiscovered(final Beacon beacon) {
        if (!isInExistenceList(beacon)) {
            //beacon sinyali kaybedilip tekrar alınmışsa ve kısa zaman once SMS atılmışsa bunu kontrol et. SMS atma.
            sendGetInServiceNotification(beacon);
            inVehicleList.put(beacon, new Date().getTime());
        }else {
            inVehicleList.put(beacon, new Date().getTime());
        }
        counter++;
    }

    private boolean isInExistenceList(Beacon beacon) {
        boolean found = false;
        if (inVehicleList.get(beacon) != null) {
            return true;
        }else {
            return false;
        }

    }
    private void sendGetInServiceNotification(Beacon beacon) {
        sendSMS("05452879744", studentList.getStudentName(beacon) + " servise bindi. BeaconId:" + beacon.getId());
    }

    private void sendGetOutServiceNotification(final Beacon beacon) {
        sendSMS("05452879744", studentList.getStudentName(beacon) + " servisten indi. BeaconId" + beacon.getId());
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void startControlInVehicleList() {
        inVehicleControlThread = new InVehicleControlThread();
        inVehicleControlThread.start();
    }

    class InVehicleControlThread extends Thread {

        public boolean goOn = true;
        @Override
        public void run() {
            Map.Entry<Beacon, Long> entry;
            while (goOn) {
                Set<Map.Entry<Beacon, Long>> entrySet = inVehicleList.entrySet();
                ArrayList<Beacon> removeList = null;

                Iterator<Map.Entry<Beacon, Long>> it = entrySet.iterator();

                while (it.hasNext()) {
                    entry = it.next();
                    if ((new Date().getTime() - entry.getValue()) > nonExistenceDuration) {
                        Date now = new Date(entry.getValue());
                        if (removeList == null) {
                            removeList = new ArrayList<>();
                        }
                        removeList.add(entry.getKey());
                    }
                }
                if (removeList != null) {
                    for (Beacon item : removeList) {


                        sendGetOutServiceNotification(item);

                        inVehicleList.remove(item);
                    }
                    removeList.clear();
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setStudentList(StudentList studentList) {
        this.studentList = studentList;
    }
}
