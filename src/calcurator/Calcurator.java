package calcurator;

import java.util.Scanner;

public class Calcurator {
	public void calculate() {
		System.out.println("数値を入力してください。");
		
		Scanner sc = new Scanner(System.in);
		int test = sc.nextInt();
		System.out.println(test + "が入力されました。");
		
		
	}
	
	/**
	 * 計算機の使用継続or終了するか
	 * @return 計算機継続フラグ（継続：true/終了：false）
	 */
	public boolean selectContinueOrEnd() {
		String printMsg = "";
		printMsg = "\n"
				+ "\n" + "まだ計算機を使用しますか？"
				+ "\n" + " [1]:使用する"
				+ "\n" + " [1以外]:終了する"
				+ "\n"
				;
		
		System.out.println(printMsg);
		
		Scanner sc = new Scanner(System.in);
		int ans = sc.nextInt();
		
		if(ans != 1) {
			return false;
		} else {
			return true;
		}
	}
}
