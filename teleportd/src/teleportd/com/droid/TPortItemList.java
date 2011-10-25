package teleportd.com.droid;

import com.google.api.client.util.Key;

import java.util.ArrayList;
import java.util.List;

public class TPortItemList {
	
	public TPortItemList(){
		i=new ArrayList<TPortItem>();
	}
	
	public static class TPortItem {
		
		public TPortItem() {
			loc=new int[2];
		}
		
		@Key String sha;
		@Key int[] loc;
		@Key int age;
		@Key int date; 		//POSIX time the picture was taken	integer
		@Key String thumb;	//Thumbnail URL (size vary with source service, generally ~100x100)	string
		@Key String full;	//full resolution URL (size vary with source service and user hardware)	string
		@Key int rank;		//hint on the ranking of the picture against the current query	integer
		int grade;
	}
	
	public static class TPortItemFull extends TPortItem {
		@Key String src;	//Service from which the picture is originating	string
		@Key UserItem user;//	author representation	object
		@Key int views;		//Number of views	integer
		@Key int likes;	 	//Number of likes	integer
		@Key int comments;	//Number of comments	integer
		@Key GidexItem qidx; //	Index information reprensentation	object

	}
	
	public static class UserItem {
		@Key String name;   //Display name	string
		@Key String pic; //	Profile picture URL (generally small)	string
		@Key String twitter; //	If defined, the author twitter handle (no @)	undefined|string

	}
	
	public static class GidexItem {
		@Key String[] strs;	//Words extracted from the comments	string array
		@Key String[] tags; //	Tags asssociated with the picture (#...)	string array
		@Key String[] srcs; //	Source service	string array
		@Key String[] usrs; //	Author handles (multiple value possible)	string array
	}
	
	@Key List<TPortItem> i;


}
