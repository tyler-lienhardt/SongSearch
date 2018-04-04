package student;
import java.util.*;

import student.Song.CmpArtist;

/*
 * SearchByArtistPrefix.java
 * starting code
 * Boothe 2016
 * 
 * student: Tyler Lienhardt
 */
public class SearchByArtistPrefix{

	private Song[] songs;  // The constructor fetches and saves a reference to the song array here
	
	public SearchByArtistPrefix(SongCollection sc) {
		songs = sc.getAllSongs();
	}

	/**
	 * Coordinates gathering of search results.
	 * 
	 * Uses binary search to find initial match or potential partial match.
	 * Then calls findLeadResult and findLastResult methods to find the range
	 * of the artist's catalog. 
	 * 
	 * Then checks if the following artist in the (sorted) 'songs' array also
	 * matches the search query (in the case of a partial query). 
	 * If the following artist also matches, then that artist's catalog is 
	 * also incorporated into the results range.
	 * 
	 * All elements in the results range are then copied over to a new results array,
	 * which is returned.
	 * 
	 * @param searchQuery
	 * @return
	 */
	public Song[] search(String searchQuery) {
	    
	    CmpArtist cmp = new CmpArtist();   //song artist comparator
        Song searchSong = new Song(searchQuery, "", "");   //storing search query as Song object for comparator
	    
	    //determines the range of matching results for first matching artist in songs array
	    int leadResult = findLeadResult(searchSong, cmp);   
	    int lastResult = findLastResult(searchSong, cmp);
	    
	    leadResult = (leadResult < 0)? (leadResult * -1) - 1 : leadResult;
	    lastResult = (lastResult < 0)? (lastResult * -1) - 1 : lastResult;
	    
	    if (lastResult - leadResult == 0) {
	        Song[] noResultsArray = new Song[0];
	        return noResultsArray;
	    }
	
	    Song[] resultsArray = resultsArrayBuilder(leadResult, lastResult);
	    
	    return resultsArray;
	}
	
	private int findLeadResult(Song searchSong, Comparator cmp){
       
       int start = 0;
       int end = songs.length-1;
       int mid = 0;
       int matchInt = -1;
       
       while (start <= end) {
           mid = (start + end) / 2;
           
           Song midSong = songs[mid];
           
           int compareInt = cmp.compare(searchSong, midSong);
           
           if (compareInt < 0) {
               end = mid - 1;
           }
           else if (compareInt > 0) {
               start = mid + 1;
           }
           else {
               matchInt = mid;
               end = mid - 1;
           }
       }
       
       if (matchInt >= 0) {
           return matchInt;
       }
       else {
           return (start * -1) - 1;
       }
   }
	
	/**
	 * Finds the index of the last occurrence of an artist in songs array.
	 * 
	 * Uses a series of binary searches to search all elements in the songs array
	 * after the startIndex, until it find the last occurrence of the specified song.
	 * 
	 * Each binary search constricts the searchable range until there is only one
	 * possible match left. This is the lastResult index that will be returned.
	 * 
	 * @param startIndex
	 * @param searchSong
	 * @param cmp
	 * @return
	 */
	private int findLastResult(Song searchSong, Comparator cmp){
	    int start = 0;
        int end = songs.length - 1;
        int mid = 0;
        int matchInt = -1;
        
        while (start <= end) {
            mid = (start + end) / 2;
            
            Song midSong = songs[mid];
            
            int compareInt = cmp.compare(searchSong, midSong);
            
            if (compareInt < 0) {
                end = mid - 1;
            }
            else if (compareInt  > 0) {
                start = mid + 1;
            }
            else {
                matchInt = mid;
                start = mid + 1;
            }
        }
        
        if (matchInt >= 0) {
            return matchInt;
        }
        else {
            return (start * -1) - 1;
        }
        
    }
	
	/**
     * Takes all elements within range of leadResult and lastResult in songs array,
     * and copies them into a new array of search results.
     * 
     * @param leadResult
     * @param lastResult
     * @return
     */
    private Song[] resultsArrayBuilder(int leadResult,int lastResult){
        int resultRange = lastResult - leadResult + 1; //+1 because of zero-indexing
        
        Song[] resultsArray = new Song[resultRange];
        int counter = 0;
        for (int i = leadResult; i <= lastResult; i++){
            resultsArray[counter++] = songs[i];
        }
        return resultsArray;    
    }

    public static void main(String[] args) {
	       
	    if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);
		
		if (args.length > 1){
			System.out.println("searching for: "+args[1]);
			Song[] byArtistResult = sbap.search(args[1]);

			//TESTS
	        //prints length of songs array
	        int numSongsTotal = byArtistResult.length;
	        System.out.println("Total songs = " + numSongsTotal + ", first songs:");
	        
	        //loop to print toString of up to first 10 songs
	        int i = 0;
	        while (i < numSongsTotal && i < 10){
	            System.out.println(byArtistResult[i].toString());
	            i++;
	        }
		}

	}
}
