package esc.voting.service;

import esc.voting.service.Song;
import io.reactivex.Maybe;

public class SongService {
    Maybe<Song> findSong(int songNumber) {
        return Song.ALL.filter(s -> s.getNumber() == songNumber).firstElement();
    }

}