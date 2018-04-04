/* Song.java
 * 
 * SongSearch searches through a list of Song objects.
 * 
 * Code written by Tyler Lienhardt
 * for COS-285 Data Structures, Fall 2017
 */

package student;

import java.util.*;

public class Song implements Comparable<Song> {
   // fields
   private String 
           artist,
           title,
           lyrics;
   
   private int hashArtist,
               hashTitle,
               rank = -1;
               
   private static int hashAmt = 4;
    
   // constructor
   public Song(String artist, String title, String lyrics) {
       this.artist = artist;
       this.title = title;
       this.lyrics = lyrics;
       
       //checks for empty parameters in case song object is dummy search song
       hashArtist = (artist == "")? -1 : makeHash(artist);
       hashTitle = (title == "")? -1 : makeHash(title);     
   }
   
   public void setRank(int rankArg){
	   rank = rankArg;
   }
   
   public int getRank(){
	   return rank;
   }

   public String getArtist() {
       return artist;
   }

   public String getLyrics() {
       return lyrics;
   }

   public String getTitle() {
       return title;
   }
   
   public int getHashTitle() {
       return hashTitle;
   }
   
   public int getHashArtist() {
       return hashArtist;
   }
   
   //converts first four (hashAmt) characters of a string into a 10 digit integer,
   //in order to save time on comparisons. 
   public static int makeHash(String s) { 
       int hashInt = 0;
       
       for (int i = 0; i < s.length() && i < hashAmt; i++) {
           hashInt += Character.toUpperCase(s.charAt(i)); 
           if (i < hashAmt - 1)
               hashInt *= 100;
       }
  
       while (hashInt < 10000000) { //10,000,000
           hashInt *= 10;
       }
       
       return hashInt;
   }
   
   public String toString() {
       return artist + ", \"" + title + "\"";
   }

   /* 
    * the default comparison of songs
    * primary key: artist, secondary key: title
    * used for sorting and searching the song array
    * if two songs have the same artist and title they are considered the same
    */
   public int compareTo(Song song2) {
       
       String song2Artist = song2.getArtist();
       String song2Title = song2.getTitle();
 
       //if both songs are by same artist, then compare titles and return result
       if (artist.equalsIgnoreCase(song2Artist)){          
           return title.compareToIgnoreCase(song2Title);
       }
       else {          
           return artist.compareToIgnoreCase(song2Artist);          
       }
   }

   public static class CmpArtist extends CmpCnt implements Comparator<Song> {
       public int compare(Song song1, Song song2){
           cmpCnt++;
           
           int song1Int = song1.getHashArtist();           
           int song2Int = song2.getHashArtist();
           
           String song1Artist = song1.getArtist();
           String song2Artist = song2.getArtist();
         
           if (song1Int <= song2Int) {
               //checks for prefix match
               if (song1Artist.regionMatches(true, 0, song2Artist, 0, song1Artist.length())) {
                   return 0;
               }
               return -1;
           }
           else {
               return 1;
           }
       }
   }
   
   public static class CmpTitle extends CmpCnt implements Comparator<Song> {
       public int compare(Song song1, Song song2) {
           cmpCnt++;
           
           int song1Int = song1.getHashTitle();           
           int song2Int = song2.getHashTitle();
           
           String song1Title = song1.getTitle();
           String song2Title = song2.getTitle();
           
           if (song1Int <= song2Int) {
               //checks for prefix match
               if (song1Title.regionMatches(true, 0, song2Title, 0, song1Title.length())) {
                   return 0;
               }              
               return -1;
           }
           else {
               return 1;
           }
           
       }
   }
   
   
   public static class CmpSong extends CmpCnt implements Comparator<Song> {
       public int compare(Song song1, Song song2) {
    	   
    	   int artistComp = compHashArtist(song1, song2);
    	   
    	   if (artistComp != 0) {
    		   return artistComp;
    	   }
    	   else {
    		   artistComp = song1.getArtist().compareTo(song2.getArtist());
    		   
    		   if (artistComp != 0) {
    			   return artistComp;
    		   }
    		   else {
    			   int titleComp = compHashTitle(song1, song2);
    			   
    			   if (titleComp != 0) {
    				   return titleComp;
    			   }
    			   else {
    				   return song1.getTitle().compareTo(song2.getTitle());
    			   }
    		   }
    	   }
       }
       
       private int compHashArtist(Song song1, Song song2) {
    	   int artist1Int = song1.getHashArtist();
    	   int artist2Int = song2.getHashArtist();
    	   
    	   if (artist1Int < artist2Int) {
    		   return -1;
    	   }
    	   else if (artist1Int > artist2Int) {
    		   return 1;
    	   }
    	   else {
    		   return 0; 
    	   }
       }
       
       private int compHashTitle(Song song1, Song song2) {
    	   int title1Int = song1.getHashTitle();
    	   int title2Int = song2.getHashTitle();
    	   
    	   if (title1Int < title2Int) {
    		   return -1;
    	   }
    	   else if (title1Int > title2Int) {
    		   return 1;
    	   }
    	   else {
    		   return 0;
    	   }
    	   
       }
   }
   
	public static class CmpRank implements Comparator<Song>{
		public int compare(Song s1, Song s2){
			if (s1.getRank() < s2.getRank())
				return -1;
			else if (s1.getRank() > s2.getRank())
				return 1;
			else
				return 0;			
		}
	}
  
   // testing method to test this class
   public static void main(String[] args) {
      Song s1 = new Song("Professor B",
            "Small Steps",
            "Write your programs in small steps\n"+
            "small steps, small steps\n"+
            "Write your programs in small steps\n"+
            "Test and debug every step of the way.\n");

      Song s2 = new Song("Brian Dill",
            "Ode to Bobby B",
            "Professor Bobby B., can't you see,\n"+
            "sometimes your data structures mystify me,\n"+
            "the biggest algorithm pro since Donald Knuth,\n"+
            "here he is, he's Robert Boothe!\n");

      Song s3 = new Song("Professor B",
            "Debugger Love",
            "I didn't used to like her\n"+
            "I stuck with what I knew\n"+
            "She was waiting there to help me,\n"+
            "but I always thought print would do\n\n"+
            "Debugger love .........\n"+
            "Now I'm so in love with you\n");

      System.out.println("testing getArtist: " + s1.getArtist());
      System.out.println("testing getTitle: " + s1.getTitle());
      System.out.println("testing getLyrics:\n" + s1.getLyrics());
    
      System.out.println("testing toString:\n");
      System.out.println("Song 1: " + s1);
      System.out.println("Song 2: " + s2);
      System.out.println("Song 3: " + s3);

      System.out.println("testing compareTo:");
      System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
      System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
      System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
      System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
      System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
      
      Song.CmpArtist CmpArtistObj = new Song.CmpArtist();
      System.out.println("\ntesting the comparator:");
      System.out.println("Song1 vs Song2 = " + CmpArtistObj.compare(s1, s2));
      System.out.println("Song2 vs Song1 = " + CmpArtistObj.compare(s2, s1));
      
   }
}
