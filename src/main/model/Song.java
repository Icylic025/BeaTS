package model;

public class Song {
    private String artist;
    private String title;
    private String filePath;
    private Beats beats;

    public Song(String artist, String title, String filePath) {
        this.artist = artist;
        this.title = title;
        this.filePath = filePath;
        this.beats = new Beats(filePath);
    }

    public int getBpm() {
        return beats.getBpm();
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    // Additional methods TODO

}
