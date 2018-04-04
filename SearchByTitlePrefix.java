/* SearchByTitlePrefix.java
 * 
 * For a query that is N letters long, this class returns any Song
 * who's title's first N letters match the query. 
 * 
 * Code written by Tyler Lienhardt
 * for COS-285 Data Structures, Fall 2017
 */

package student;

import student.AddendumList.L2Array;
import student.Song.CmpTitle;
import java.util.*;


//TODO: figure out why this method is not searching correctly. Probably hashing error.

public class SearchByTitlePrefix {

    private Song[] songs;
    CmpTitle cmp = new CmpTitle();
    
    private AddendumList<Song> songsAL = new AddendumList(cmp);
    
    //constructor
    public SearchByTitlePrefix(SongCollection sc) {
        songs = sc.getAllSongs();   
        
    for (Song currentSong : songs) {
        songsAL.add(currentSong);
    }
       
      //Added this "dummy song" at the end of the list 
      //to avoid indexOutOfBounds Exceptions when creating resultsSubList
        songsAL.add(new Song("","~~~",""));
  
      //flattening AddendumList for the sake of findFirstInArray calls
        songsAL.mergeAllLevels();
        
        //System.out.println("Comparisons to build songs Addendum List: " + cmp.getCmpCnt());
    }
    
    public Song[] search(String searchQuery) {
        cmp.resetCmpCnt();
       
        L2Array searchL2 = (L2Array)songsAL.l1array[0];
        
        Song searchSong = new Song("", searchQuery, "");
        
        int startIndex = songsAL.findFirstInArray(searchSong, searchL2);
        int endIndex = songsAL.findLastInArray(searchSong, searchL2);
        
      //endIndex + 1 because subList method uses exclusive end-boundary
        int resultsRange = (endIndex + 1) - startIndex;
        
        if (startIndex < 0) {
            Song[] noResults = new Song[0];
            return noResults;
        }
        else {
            //Song startSong = (Song)searchL2.items[startIndex];
            //Song endSong = (Song)searchL2.items[endIndex + 1];
            
            //AddendumList<Song> resultsSubList = songsAL.subList(startSong, endSong);
            
            Song[] searchResults = new Song[resultsRange];
            
            System.arraycopy(searchL2.items, startIndex, searchResults, 0, resultsRange);
                    
            //resultsSubList.toArray(searchResults);
            
            System.out.println("Comparisons to search query '" 
                        + searchQuery + "': " + cmp.getCmpCnt());
        
            return searchResults;
        }
    }
    
    public static void main(String[] args){
        
        if (args.length == 0) {
            System.out.println("Error: add arguments");
        }
        
        SongCollection sc = new SongCollection(args[0]);
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);
        
        if (args.length > 1){
            System.out.println("Searching for: " + args[1]);
            Song[] searchResults = sbtp.search(args[1]);
            
            //TESTS
            int numSongsTotal = searchResults.length;
            System.out.println("Total songs = " + numSongsTotal + ", first songs:");
            
            //loop to print toString of first 10 songs
            int i = 0;
            while (i < numSongsTotal && i < 10){
               System.out.println(searchResults[i++].toString()); 
            }           
        }
    }
}
