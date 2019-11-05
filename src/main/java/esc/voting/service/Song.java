package esc.voting.service;

import io.reactivex.Flowable;

public class Song {
    private final int number;
    private final String country;
    private final String name;
    private final String artist;

    public Song(int number, String country, String name, String artist) {
        this.number = number;
        this.country = country;
        this.name = name;
        this.artist = artist;
    }

    public int getNumber() {
        return number;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public static final Flowable<Song> ALL = Flowable.fromArray(
        new Song(1, "Malta", "Michela", "Chameleon"),
        new Song(2, "Albania", "Jonida Maliqi", "Ktheju tokës"),
        new Song(3, "Czech Republic", "Lake Malawi", "Friend of a Friend"),
        new Song(4, "Germany", "S!sters", "Sister"),
        new Song(5, "Russia", "Sergey Lazarev", "Scream"),
        new Song(6, "Denmark", "Leonora", "Love Is Forever"),
        new Song(7, "San Marino", "Serhat", "Say Na Na Na"),
        new Song(8, "North Macedonia", "Tamara Todevska", "Proud"),
        new Song(9, "Sweden", "John Lundvik", "Too Late for Love"),
        new Song(10, "Slovenia", "Zala Kralj & Gašper Šantl", "Sebi"),
        new Song(11, "Cyprus", "Tamta", "Replay"),
        new Song(12, "Netherlands", "Duncan Laurence", "Arcade"),
        new Song(13, "Greece", "Katerine Duska", "Better Love"),
        new Song(14, "Israel", "Kobi Marimi", "Home"),
        new Song(15, "Norway", "KEiiNO", "Spirit in the Sky"),
        new Song(16, "United Kingdom", "Michael Rice", "Bigger than Us"),
        new Song(17, "Iceland", "Hatari", "Hatrið mun sigra"),
        new Song(18, "Estonia", "Victor Crone", "Storm"),
        new Song(19, "Belarus", "ZENA", "Like It"),
        new Song(20, "Azerbaijan", "Chingiz", "Truth"),
        new Song(21, "France", "Bilal Hassani", "Roi"),
        new Song(22, "Italy", "Mahmood", "Soldi"),
        new Song(23, "Serbia", "Nevena Božović", "Kruna (Круна)"),
        new Song(24, "Switzerland", "Luca Hänni", "She Got Me"),
        new Song(25, "Australia", "Kate Miller-Heidke", "Zero Gravity"),
        new Song(26, "Spain", "Miki", "La Venda")
    );

}
