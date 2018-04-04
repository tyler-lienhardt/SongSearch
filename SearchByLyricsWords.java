/* SearchByLyricsWords.java
 * 
 * This class takes a search query of words, and returns any song who's
 * lyrics contain those words, regardless of order.
 * 
 * Code written by Tyler Lienhardt
 * for COS-285 Data Structures, Fall 2017
 */

package student;

import java.util.*;
import student.Song.CmpSong;


public class SearchByLyricsWords {
   
    private Song[] songs;
    Comparator<Song> compSong = new CmpSong();
    Map<String, Set<Song>> wordMap = new TreeMap<>();
    Set<String> comWordsSet = new HashSet<>();
    String[] comWordsArr;
    
    
    private String comWordsStr = " the of and a to in is you that it he for was on \r\n" + 
    		" are as with his they at be this from I have or \r\n" + 
    		" by one had not but what all were when we there \r\n" + 
    		" can an your which their if do will each how them \r\n" + 
    		" then she many some so these would into has more \r\n" + 
    		" her two him see could no make than been its now \r\n" + 
    		" my made did get our me too a b c d e f g h i j k l m \r\n"
			 	+ " n o p q r s t u v w x y z ";
    
    public SearchByLyricsWords(SongCollection sc) {
    	
    	songs = sc.getAllSongs(); 
        
        //building wordMap
        for (Song song : songs) {
        	
			String lyrics = song.getLyrics();
						
			lyrics = lyrics.toLowerCase();
			
			String[] wordsArr = lyrics.split("[^a-z]+");
			
			Set<String> wordsSet = arrToSet(wordsArr);
			
			for (String word : wordsSet) {
				if (wordMap.containsKey(word)) {
						//adding the associated song to that word's song set
						wordMap.get(word).add(song);
					}
				else {
					//creating the song set if it does not exist yet
					Set<Song> valueSet = new TreeSet<>(compSong);
					valueSet.add(song);
					wordMap.put(word, valueSet);
				}
			}   	
        }    
    }
    
    
    public Song[] search(String query) {
    	//declaring this here for use in several places in for loop
    	Song[] noResults = new Song[0];
    	
    	query = query.toLowerCase();	
    	String[] queryWords = query.split("[^a-zA-Z]+");	
    	Set<String> querySet = arrToSet(queryWords);
    	
    	Set<RankedWord> rWordSet = new TreeSet<>();
    	
    	for (String word : querySet) {
    		if (wordMap.containsKey(word)){
    			RankedWord rWord = new RankedWord(wordMap.get(word).size(), word);
    			rWordSet.add(rWord);
    		}
    		else {
    			return noResults;
    		}
    	}
    	
    	Set<Song> resultsSet = new TreeSet<>();
    	
    	for (RankedWord rWord : rWordSet) {
    		
    		if (wordMap.containsKey(rWord.word)) {
    			if (resultsSet.isEmpty()) {
    				resultsSet.addAll(wordMap.get(rWord.word));
    			}
    			else {
    				resultsSet.retainAll(wordMap.get(rWord.word));
    				if (resultsSet.isEmpty()) {
    					return noResults;
    				}
    			}
    		}
    		else {
    			return noResults;
    		}
    	}
    	
    	Song[] resultsArr = resultsSet.toArray(new Song[resultsSet.size()]);
    	
    	return resultsArr;
    }
    
    private Set<String> arrToSet(String[] a){
    	Set<String> set = new HashSet<>();
    	
    	for (String word : a) {
    			set.add(word);
    	}
    	
    	return set;
    }
        
    static class RankedWord implements Comparable<RankedWord>{
    	int numSongs;
    	String word;
    	
    	RankedWord(int numSongsArg, String wordArg){
    		numSongs = numSongsArg;
    		word = wordArg;
    	}
    	
    	public int compareTo(RankedWord word2){
    		return this.numSongs - word2.numSongs;
    	}
    }
    
    public void statistics() {
    	System.out.println("Number of keys (expecting 31385): " + wordMap.size());
    	
    	int songSum = 0;
    	for (String keyWord : wordMap.keySet()) {
    		songSum += wordMap.get(keyWord).size();
    	}
    	
    	System.out.println("Number of song mappings (expecting 637601): " + songSum);
    	
    	System.out.println("Space used by TreeMap (6K, K = number of keys): " + (wordMap.size() * 6));
    	
    	System.out.println("Total space used by TreeSets (6V, V = number of values): " + (songSum * 6));
    	
    	System.out.println("Total space used by entire compound data structure (6K + 6V): " + ((wordMap.size() * 6) + (songSum * 6)));
    }
    
    public void testPrint(int numToPrint) {
    	System.out.println("Printing wordMap...");
    	int i = 0;
    	
    	for (String word : wordMap.keySet()) {
    		
    		System.out.print(word + ": {");
    		
    		Object[] songArray = wordMap.get(word).toArray();
    		for (Object song : songArray) {
    			System.out.print(((Song) song).getArtist() + " - " + ((Song) song).getTitle() + ", ");
    		}
    		
    		System.out.println("}");
    		
    		i++;
    		if (i == numToPrint)
    			break;
    	}
    	
    }
    
    public static void main (String[] args) {
    	if (args.length == 0) {
            System.out.println("Error: add arguments");
        }
    	else {
    		SongCollection sc = new SongCollection(args[0]);
    		   		
    		SearchByLyricsWords sblw = new SearchByLyricsWords(sc);
    		
    		Song[] songs = sblw.songs;
    		
    		sblw.statistics();
    		
    		if (args.length > 1) {
    			Song[] results = sblw.search(args[1]);
    			System.out.println("\nResults found for \"" + args[1] + "\": " + results.length);
    			for (int i = 0; i < results.length && i < 10; i++){
    				System.out.println(results[i].getArtist() + " - " + results[i].getTitle());
    			}
    		}
    		else {
    			System.out.println("No search argument provided.");
    		}
    	}
    	
    		    
    }
}
