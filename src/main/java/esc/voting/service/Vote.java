package esc.voting.service;

import java.time.Instant;

public class Vote {
    private String clientIpAddress;
    private Instant receptionTime;
    private int songNumber;
    private String voterName;

    public Vote() {
    }

    public Vote(int songNumber, String voterName) {
        this.clientIpAddress = null;
        this.receptionTime = Instant.now();
        this.songNumber = songNumber;
        this.voterName = voterName;
    }

    public Vote(String clientIpAddress, Instant receptionTime, int songNumber, String voterName) {
        this.clientIpAddress = clientIpAddress;
        this.receptionTime = receptionTime;
        this.songNumber = songNumber;
        this.voterName = voterName;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public Instant getReceptionTime() {
        return receptionTime;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public String getVoterName() {
        return voterName;
    }
    
    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public void setReceptionTime(Instant receptionTime) {
        this.receptionTime = receptionTime;
    }
    
    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    } 
    
    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }
}
