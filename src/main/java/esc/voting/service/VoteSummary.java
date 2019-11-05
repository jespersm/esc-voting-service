package esc.voting.service;

public class VoteSummary {
    private final long[] votes;

    public VoteSummary(int maxSongNumber) {
        votes = new long[maxSongNumber+1];
    }

    public long getVotesFor(int songNumber) {
        return votes[songNumber];
    }
}
