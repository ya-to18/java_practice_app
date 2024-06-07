package sugoroku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
	
	//===================
	// メンバー変数
	//===================
	
	/**
	 * 各マスごとのイベント内容リスト
	 */
	private List<String> squareContentList = new ArrayList<String>() {
		{
			add("なし。");
			add("少し眠くなった。1回休み。");
			add("体が軽い。2マス進む。");
			add("なし。");
			add("なし。");
			add("後ろ歩きしてみた。1マス戻る。");
			add("なし。");
			add("レンタカーを借りた。次は2マス進む。");
			add("なぜかゴールに到着する。");
			add("用事を思い出した。3マス戻る。");
		}
	};
	
	/**
	 * 各マスごとの行動休み付与回数リスト
	 */
	private List<Integer> addBreakNumList = new ArrayList<Integer>() {
		{
			add(0);
			add(1);
			add(0);
			add(0);
			add(0);
			add(0);
			add(0);
			add(0);
			add(0);
			add(0);
		}
	};
	
	/**
	 * 各マスごとの加算マス数リスト
	 */
	private List<Integer> addPosNumList = new ArrayList<Integer>() {
		{
			add(0);
			add(0);
			add(2);
			add(0);
			add(0);
			add(-1);
			add(0);
			add(0);
			add(99);
			add(-3);
		}
	};
	
	/**
	 * 各マスごとの移動倍率リスト
	 */
	private List<Integer> moveMultipleList = new ArrayList<Integer>() {
		{
			add(1);
			add(1);
			add(1);
			add(1);
			add(1);
			add(1);
			add(1);
			add(2);
			add(1);
			add(1);
		}
	};

	/**
	 * マップのマス数
	 */
	private int squareNum = this.squareContentList.size();
	
	/**
	 * ゲーム起動
	 */
	public void startUp() {
		System.out.println("Game startUp start");
		
		//============
		// ゲームループ
		//============
		boolean isGameContinue = true; // ゲーム継続フラグ(true:継続/false:終了)
		
		while(isGameContinue) {
			//-----------------
			// ゲーム開始選択
			//-----------------
			int selectedGameStart = 0; // ゲーム開始選択値(1:開始/1以外:終了)
			selectedGameStart = this.selectGameStart();
			
			//-----------------
			// ゲーム開始終了分岐
			//-----------------
			// ゲーム終了(1以外)が入力された場合
			if(selectedGameStart != 1) {
				//----------------
				// ゲーム終了
				//----------------
				this.printGameFinish();
				
				isGameContinue = false; // ゲーム継続フラグ(false:終了)
			}
			// ゲーム開始(1)が入力された場合
			else {
				//----------------
				// ゲームプレイ処理
				//----------------
				// ゴールに到達した場合はfalse(終了)、途中終了した場合はtrue(継続)
				isGameContinue = this.playGame();
			}
		}

		System.out.println("Game startUp end");
	}
	
	/**
	 * ゲームプレイ
	 * @return ゲーム継続フラグ(true:継続/false:終了)
	 */
	private boolean playGame() {
		boolean isGameContinue = true; // ゲーム継続フラグ(true:継続/false:終了)
		
		// プレイヤー情報 --------------------
		int currentPosition = 0;        // 現在位置のマス数(スタート地点を0とする)
		int breakNum = 0;               // 行動休み回数
		int moveMultiple = 1;           // 移動倍率
		//----------------------------------
		
		//---------------------
		// マップを書く※初期表示
		//---------------------
		this.printMap(this.squareContentList, currentPosition);
		
		//=============
		// 行動ループ
		//=============
		boolean isActionContinue = true;
		
		while(isActionContinue) {
			//---------------
			// 移動、終了選択
			//---------------
			int selectedMoveOrStop = 0; // 移動終了選択(1:移動/1以外:途中終了)
			selectedMoveOrStop = this.selectMoveOrStop();
			
			//---------------
			// 移動途中終了分岐
			//---------------
			// 途中終了(1以外)が入力された場合
			if(selectedMoveOrStop != 1) {
				break; // 行動ループから抜ける
			}
			// 移動(1)が入力された場合
			else {
				//-----------------
				// 休み処理
				//------------------
				if(breakNum > 0) {
					// 休みメッセージ出力
					this.printBreakTime();
					
					breakNum -= 1; // 休み回数から1回分引く
					continue;   // 次のループへ
				}
				
				//------------------
				// 移動する
				//------------------
				// 移動マス数を計算
				int moveValue = this.calculateMoveValue(moveMultiple);
				
				// 移動後の現在位置マス数を計算
				currentPosition += moveValue;
				
				//-------------------
				// マップを書く※移動後
				//-------------------
				this.printMap(squareContentList, currentPosition);
				
				//-------------------
				// イベント発生
				//-------------------
				// 現在位置マス数がゴールまでのマス数より大きい場合は、
				// ゴールしていると想定されるためイベント発生の処理をしない
				if(currentPosition <= this.squareNum) {
					//------------------------
					// 発生したイベント内容を出力
					//------------------------
					this.printEvent(this.squareContentList, currentPosition);
					
					//---------------------
					// イベント効果を反映
					//---------------------
					// 現在位置マス数−1がリストのIndex位置と対応している
					int listIndex = currentPosition -1;
					
					// 行動休み回数
					breakNum = this.addBreakNumList.get(listIndex);
					// 現在位置にイベント効果のマス数を加算
					currentPosition += this.addPosNumList.get(listIndex);
					// 移動倍率
					moveMultiple = this.moveMultipleList.get(listIndex);
				}
				
				//------------------
				// ゴール到達分岐
				//------------------
				// ゴールに到達した場合
				if(currentPosition > this.squareNum) {
					//------------------
					// ゴール
					//------------------
					this.printGameClear();
					
					isActionContinue = false;      // 行動継続フラグ(false:終了)
					isGameContinue = false;        // ゲーム継続フラグ(false:終了)
				}
				// ゴールに到達していない場合
				else {
					//-------------------------
					// マップを書く ※イベント発生後
					//-------------------------
					this.printMap(this.squareContentList, currentPosition);
				}
			}
		}
		
		return isGameContinue;
	}
	
	/**
	 * コンソールでゲーム開始または終了を選択する
	 * @return コンソールで入力した数値
	 */
	private int selectGameStart() {
		// 選択要求メッセージ
		String requestMsg = "";
		requestMsg = ""
				+ "\n"
				+ "\n" + "**************************"
				+ "\n" + "*"
				+ "\n" + "* すごろくゲーム"
				+ "\n" + "*"
				+ "\n" + "**************************"
				+ "\n" + "★ キーボードから以下の数値を入力してください。"
				+ "\n" + "  [1]:ゲームを開始する。"
				+ "\n" + "  [1以外]:ゲームを終了する。"
				+ "\n"
				;
		
		System.out.println(requestMsg);
		
		// コンソールから入力値を受け取る
		Scanner sc = new Scanner(System.in);
		int inputNo = sc.nextInt();
		
		return inputNo;
	}
	
	private void printGameFinish() {
		// ゲーム終了メッセージ
		String printMsg = "";
		printMsg = ""
				+ "\n"
				+ "\n" + "ゲームを終了しました。"
				+ "\n"
				;
		
		System.out.println(printMsg);
	}
	
	/**
	 * マップを書く
	 * スタートからゴールまでのマスを○で出力する
	 * ※ただし、現在位置のマスは●で出力する
	 * 各マスにイベント内容を出力する
	 * @param contentList 各マスごとのイベント内容リスト
	 * @param currentPos 現在位置のマス数
	 */
	private void printMap(List<String> contentList, int currentPos) {
		
		// スタートを出力する
		System.out.println("スタート" + "\n" + "！");
		
		// スタートからゴールまでのマスとイベント内容を出力する
		for(int index = 0; index < contentList.size(); index++) {
			int loopCnt = index + 1;     // ループ回数
			String printSquare = "";     // 1マスに出力する内容
			
			//------------------------
			// マスの出力内容を分岐する
			//------------------------
			String squareKind = "◯";     // デフォルトのマスは◯で出力する
			
			// ループ回数目が現在位置のマス数と同じか？
			if(loopCnt == currentPos) {
				squareKind = "● ";       // 現在位置のマスは●で出力する
			}
			
			//--------------------------
			// イベント内容をリストから取得
			//--------------------------
			String eventContent = contentList.get(index);
			
			//--------------------------
			// 各マスに出力する内容を生成
			// ◯　イベント内容
			// |
			//--------------------------
			printSquare += squareKind + "      " + eventContent;
			printSquare += "\n" + "|";
			
			System.out.println(printSquare);
		}
		
		// 「ゴール」を出力する
		System.out.println("ゴール");
	}
	
	/**
	 * コンソールで移動またはゲーム途中終了を選択する
	 * @return コンソールで入力した数値 
	 */
	private int selectMoveOrStop() {
		// 選択要求メッセージ
		String requestMsg = "";
		requestMsg = ""
				+ "\n" + "★ キーボードから以下の数値を入力してください。"
				+ "\n" + "  [1]:移動する。"
				+ "\n" + "  [9]:ゲームを途中で終了して、開始画面に戻る。"
				+ "\n"
				;
		
		System.out.println(requestMsg);
		
		// コンソールから入力値を受け取る
		Scanner sc = new Scanner(System.in);
		int inputNo = sc.nextInt();
		
		return inputNo;
	}
	
	/**
	 * 移動するマス数を計算する
	 * @return 移動するマス数
	 */
	private int calculateMoveValue(int moveMultiple) {
		//--------------------
		// 通常の移動マス数を取得
		//--------------------
		// 1~3の範囲でランダムな数値を取得する
		int val = this.getRandomValue(3);
		
		//--------------------------
		// イベントの効果を移動力に反映
		//--------------------------
		// 移動倍率をかける
		val = val * moveMultiple;
		
		return val;
	}
	
	/**
	 * ランダムな数値を取得する
	 * @params max ランダム値の最大値
	 * @return ランダム値
	 */
	private int getRandomValue(int max) {
		Random rand = new Random();
		int val = rand.nextInt(max) + 1;
		return val;
	}
	
	/**
	 * 発生したイベント内容を出力する
	 * @param contentList イベント内容リスト
	 * @param currentPost 現在位置マス
	 */
	private void printEvent(List<String> contentList, int currentPos) {
		String printEvent = "";     // 出力するイベント
		
		// 現在位置マスのイベント内容をリストから取得
		// ※リストのIndexは0始まりのため現在位置マス−1
		String eventContent = contentList.get(currentPos - 1);
		
		// 出力用の形式に整える
		printEvent = ""
				+ "\n"
				+ "\n" + "----------------------------------------"
				+ "\n" + "★ ★ ★ イベント発生 ★ ★ ★"
				+ "\n" + " " + eventContent
				+ "\n" + "----------------------------------------"
				+ "\n"
				;
		
		System.out.println(printEvent);
	}
	
	/**
	 * ゲームクリアのメッセージを出力する
	 */
	private void printGameClear() {
		// ゲームクリアメッセージ
		String printMsg = "";
		printMsg = ""
				+ "\n"
				+ "\n" + "★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★"
				+ "\n" + " おめでとうございます！"
				+ "\n" + " ゲームをクリアしました！！"
				+ "\n" + ""
				+ "\n" + "★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★"
				+ "\n"
				;
		
		System.out.println(printMsg);
	}
	
	/**
	 * 休み中のメッセージを出力する
	 */
	private void printBreakTime() {
		// 休みメッセージ
		String printMsg = "";
		printMsg = ""
				+ "\n" + "---------------------------------"
				+ "\n" + " 休み中。"
				+ "\n" + "今回は何もしない。"
				+ "\n" + "---------------------------------"
				+ "\n"
				;
		
		System.out.println(printMsg);
	}
}
