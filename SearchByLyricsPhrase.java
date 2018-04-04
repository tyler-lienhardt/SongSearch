/* SearchByLyricsPhrase.java
 * 
 * This class takes a search query of words, and returns any song that
 * contains those words in the same order as the query. The matching 
 * words do not need to appear consecutively in the lyrics. Songs are 
 * ordered by a ranking system, where the further apart the words are 
 * in the lyrics, the lower on the list of results it will be.
 * 
 * Code written by Tyler Lienhardt
 * for COS-285 Data Structures, Fall 2017
 */

package student;
import java.util.*;
import student.Song.CmpRank;

public class SearchByLyricsPhrase {
	private static Song[] songs;
	SearchByLyricsWords sblw;
	
	public SearchByLyricsPhrase(SongCollection sc){
		songs = sc.getAllSongs();
		sblw = new SearchByLyricsWords(sc);
	}
	
	public Song[] search(String query){
		Comparator cmp = new CmpRank();
		PriorityQueue<Song> pq = new PriorityQueue(128, cmp);
		
		Song[] sblwResults = sblw.search(query);
		
		for (Song song : sblwResults){
			int rank = PhraseRanking.rankPhrase(song.getLyrics(), query);
			
			if (rank > 0){
				song.setRank(rank);
				pq.add(song);
			}
		}
		
		Song[] results = new Song[pq.size()];
		
		for (int i = 0; i < results.length; i++){
			results[i] = pq.poll();
		}
		
		return results;
	}
	
	private static void printResults(Song[] results){
		System.out.println("Total songs = " + results.length + ", first 10 matches:");
		System.out.println("rank artist title");
		
		for (int i = 0; i < results.length && i < 10; i++){
			System.out.println(results[i].getRank() + " " + results[i].getArtist() 
								+ ", \"" + results[i].getTitle() + "\"");
		}
		System.out.println("...");
	}
	
	public static void main(String[] args){
		if (args.length > 0){
			SongCollection sc = new SongCollection(args[0]);
			
			SearchByLyricsPhrase sblp = new SearchByLyricsPhrase(sc);
			
			Song[] results = sblp.search(args[1]);
			
			System.out.println("Searching for: " + args[1]);
			printResults(results);
		}
		
	}
}
