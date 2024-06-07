package calcurator;

public class Main {

	public static void main(String[] args) {
		System.out.println("計算機アプリを開始します。");
		
		boolean isCalContinue = true; // 計算機使用継続フラグ
		
		while(isCalContinue) {
			isCalContinue = new Calcurator().selectContinueOrEnd();
		}
		
		System.out.println("計算機アプリを終了します。");
	}

}
