package xyz.praveen.privateblog;

public class Interface {
	/**
	 * To change the current fragment
	 * 
	 * @author praveen
	 */
	public interface FragmentChanger {
		/**
		 * Move to URL change fragment
		 */
		public void changeUrl();

		/**
		 * Move to posting fragment
		 */
		public void startPost();
	}

}
