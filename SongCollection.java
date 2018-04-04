/* SongCollection.java
 * 
 * This class builds an array of song objects from specially formatted
 * raw text.
 * 
 * Code written by Tyler Lienhardt
 * for COS-285 Data Structures, Fall 2017
 */

package student;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class SongCollection {

	private Song[] songs;

	public SongCollection(String filename) {
        
        BufferedReader buffRdr = null;
        
        List<Song> tempSongArrayList = new ArrayList<Song>();
        
        String artist,
               title,
               lyrics = "no data",
               line,
               lyricBoundary = "\"";
        
        int subStringStartIndex,
            subStringEndIndex;
             
        try {
            buffRdr = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not find the file.");
            return;
        }
        catch (Exception e) {
            System.out.println("An error occured");
        }
        
        //reading in data
        try {
            while ((line = buffRdr.readLine()) != null){
                
                StringBuilder lyricsBldr = new StringBuilder();
                
                subStringStartIndex = line.indexOf('"') + 1;
                subStringEndIndex = line.lastIndexOf('"');
                artist = line.substring(subStringStartIndex, subStringEndIndex);
                
                line = buffRdr.readLine();
                subStringStartIndex = line.indexOf('"') + 1;
                subStringEndIndex = line.lastIndexOf('"');
                title = line.substring(subStringStartIndex, subStringEndIndex);
                
                //assembling the lyrics string, 
                while ((line = buffRdr.readLine()) != null) {
                    if (!line.equals(lyricBoundary)){
                        lyricsBldr.append(line);
                        lyricsBldr.append("\r\n");
                    }
                    else {
                        break;
                    }
                }
                
                //cutting out LYRICS=" from lyrics string
                lyrics = lyricsBldr.toString();
                subStringStartIndex = lyrics.indexOf('"') + 1;
                subStringEndIndex = lyrics.length();
                lyrics = lyrics.substring(subStringStartIndex, subStringEndIndex);
                
                tempSongArrayList.add(new Song(artist, title, lyrics));
            }
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("SC ERROR: Index out of bounds. Check file text formatting.");
        }
        catch (Exception e){
            System.out.println("SC ERROR: Something went wrong");
        }
        
        songs = new Song[tempSongArrayList.size()];
        tempSongArrayList.toArray(songs);
        
        
        Arrays.sort(songs);      
    }

	// returns the array of all Songs
	// this is used as the data source for building other data structures
	public Song[] getAllSongs() {
		return songs;
	}

	// testing method
	public static void main(String[] args) {
	    
	    if (args.length == 0) {
			System.err.println("usage: prog songfile");
			return;
		}
	    
        SongCollection sc = new SongCollection(args[0]);
        
		//TESTS
		//prints length of songs array
		int numSongsTotal = sc.getAllSongs().length;
		System.out.println("Total songs = " + numSongsTotal + ", first songs:");
		
		
		int i = 0;
		while (i < numSongsTotal && i < 10){
		    System.out.println(sc.getAllSongs()[i].toString());
		    i++;
		}
	}
}
