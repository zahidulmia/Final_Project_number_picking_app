using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Events;
using UnityEngine.SceneManagement;

[System.Serializable]
public class CustomEvents : UnityEvent{} 

public class GameController : MonoBehaviour {

	//QuestionText Object
	public GameObject Question;
	public GameObject UserScore;

	//Enemies
	public GameObject[] Enemies;
	public GameObject Samples;

	//MyAnswer Text Objects
	public GameObject EnemyText1;
	public GameObject EnemyText2;
	public GameObject EnemyText3;

	//UI Score
	public GameObject WinDisplayScore;
	public GameObject LoseDisplayScore;

	public int Num1;
	public int Num2;
	public int Answer;
	public bool ListeningToOrders = true;
	public static int Score = 0;
	// Use this for initialization
	public GameObject CheckingSelectedSample;

	public CustomEvents UserWinsEvents;
	public CustomEvents UserLosesEvents;
	void Start () {
		Score = PlayerPrefs.GetInt ("Score",0);
/*
		EnemyText1 = GameObject.Find ("Enemy1Text");
		EnemyText2 = GameObject.Find ("Enemy2Text");
		EnemyText3 = GameObject.Find ("Enemy3Text");
	
		Question = GameObject.Find ("QuestionText");
		UserScore = GameObject.Find ("Score");
*/
		//SetUserScore
		UserScore.GetComponent<Text>().text = "Score : " + Score.ToString();


		Num1 = Random.Range (0,10);
		Num2 = Random.Range (0,10);
		Answer = Num1 + Num2;

		Question.GetComponent<Text>().text = "What is " + Num1.ToString() + " + " + Num2.ToString() + "?";


		bool MoveOn = true;
		int ran;
		while(MoveOn)
		{
			ran = Random.Range (0, 20);
			EnemyText2.GetComponent<TextMesh> ().text = ran.ToString();
			if (ran != Answer)
				MoveOn = false;				
		}

		MoveOn = true;
		while(MoveOn)
		{
			ran = Random.Range (0, 20);
			EnemyText1.GetComponent<TextMesh> ().text = ran.ToString();
			if (ran != Answer)
				MoveOn = false;				
		}
		MoveOn = true;
		while(MoveOn)
		{
			ran = Random.Range (0, 20);
			EnemyText3.GetComponent<TextMesh> ().text = ran.ToString();
			if (ran != Answer)
				MoveOn = false;				
		}

		//EnemyText2.GetComponent<TextMesh> ().text = Random.Range (0,20).ToString();
		//EnemyText3.GetComponent<TextMesh> ().text = Random.Range (0,20).ToString();

		//Show Right Answer Randomly on one of the Ball
		int ShowRightAnswerOn = Random.Range (0,99);
		if (ShowRightAnswerOn % 2 == 0) {
			AssignRightAnswer (EnemyText1);
		} else if (ShowRightAnswerOn % 5 == 0) {
			AssignRightAnswer (EnemyText2);
		} else {
			AssignRightAnswer (EnemyText3);
		}


		GenerateRandomLevel ();

	}
		
	void GenerateRandomLevel()
	{
		int totalSamples = Samples.transform.childCount;

		int SampleSelected = Random.Range (0,totalSamples);

		CheckingSelectedSample = Samples.transform.GetChild (SampleSelected).gameObject;
		CheckingSelectedSample.SetActive (true);

		Enemies[0].transform.position =  CheckingSelectedSample.transform.GetChild (1).gameObject.transform.GetChild (0).transform.position;
		Enemies[1].transform.position =  CheckingSelectedSample.transform.GetChild (1).gameObject.transform.GetChild (1).transform.position;
		Enemies[2].transform.position =  CheckingSelectedSample.transform.GetChild (1).gameObject.transform.GetChild (2).transform.position;
	}
	public void AssignRightAnswer(GameObject x)
	{
		x.GetComponent<TextMesh> ().text = Answer.ToString ();

		//Set bool Right Answer In his parent gameobjetc of text to true for detection
		x.transform.parent.gameObject.GetComponent<Enemy>().RightAnswer = true;

	}
	// Update is called once per frame
	void Update () {
		
	}

	public void UserWins()
	{
		if (ListeningToOrders) {
			Score += 10;
			WinDisplayScore.GetComponent<Text>().text = "Score :" +  Score.ToString();
			UserWinsEvents.Invoke ();
			ListeningToOrders = false;
			Invoke ("ReloadScene", 3f);
		}
	}
	public void UserLoses()
	{
		if (ListeningToOrders) {
			LoseDisplayScore.GetComponent<Text>().text = "Score :" + Score.ToString();
			UserLosesEvents.Invoke ();
			ListeningToOrders = false;
//			Invoke ("ReloadScene", 3f);
		}
	}

	public void ReloadScene()
	{
		SaveUserScore ();
		SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex);
	}

	void SaveUserScore()
	{
		PlayerPrefs.SetInt ("Score",Score);

	}

}
