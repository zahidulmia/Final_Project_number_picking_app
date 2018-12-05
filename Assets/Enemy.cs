using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Enemy : MonoBehaviour {

	public GameObject deathEffect;
	//used to detect if the enemy has the right answer
	public bool RightAnswer = false;
	public float health = 4f;

	public static int EnemiesAlive = 0;

	void Start ()
	{
		EnemiesAlive++;
	}

	void OnCollisionEnter2D (Collision2D colInfo)
	{
		if (colInfo.relativeVelocity.magnitude > health)
		{
			Die();
			CheckIfThisIsTheRightAnswer ();
		}
	}

	void Die ()
	{
		Instantiate(deathEffect, transform.position, Quaternion.identity);

		EnemiesAlive--;
		if (EnemiesAlive <= 0)
			Debug.Log("LEVEL WON!");

		Destroy(gameObject);
	}
	public void CheckIfThisIsTheRightAnswer ()
	{
		if (RightAnswer)
			GameObject.Find ("GameController").GetComponent<GameController> ().UserWins ();
		if (!RightAnswer)
			GameObject.Find ("GameController").GetComponent<GameController> ().UserLoses ();


	}
}
