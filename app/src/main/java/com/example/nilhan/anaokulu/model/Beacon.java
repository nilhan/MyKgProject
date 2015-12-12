package com.example.nilhan.anaokulu.model;

import com.example.nilhan.anaokulu.util.Util;

/**
 * Created by nilhan on 06.12.2015.
 */
public class Beacon {
    private String uuid;
    private int major;
    private int minor;

    public Beacon(String uuid, int major, int minor) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode() + major + minor;
    }

    @Override
    public boolean equals(Object o) {
        // TODO Auto-generated method stub
        Beacon beaconObj = (Beacon) o;
        if (beaconObj.getMajor() == this.getMajor() &&
                beaconObj.getMinor() == this.getMinor() &&
                beaconObj.getUuid().equalsIgnoreCase(this.getUuid())) {
            return true;
        } else {
            return false;
        }
    }

    public static Beacon constructBeacon(byte[] scanRecord) {
        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                patternFound = true;
                break;
            }
            startByte++;
        }

        if (patternFound) {
            //Convert to hex String
            byte[] uuidBytes = new byte[16];
            System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
            String hexString = Util.bytesToHex(uuidBytes);

            //Here is your UUID
            String localUuid = hexString.substring(0, 8) + "-" +
                    hexString.substring(8, 12) + "-" +
                    hexString.substring(12, 16) + "-" +
                    hexString.substring(16, 20) + "-" +
                    hexString.substring(20, 32);

            //Here is your Major value
            int localMajor = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

            //Here is your Minor value
            int localMinor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);
            return new Beacon(localUuid, localMajor, localMinor);
        }

        throw new RuntimeException("Beacon Not Constructed");
    }

    public String getId() {
        return uuid+"_"+getMajor()+"_"+getMinor();
    }
}
